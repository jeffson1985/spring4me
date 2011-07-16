package org.osforce.spring4me.web.cache;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.GenericResponseWrapper;
import net.sf.ehcache.constructs.web.PageInfo;
import net.sf.ehcache.constructs.web.filter.PageFragmentCachingFilter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 21, 2011 - 8:24:02 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
@Deprecated
public class WidgetCachingFilter extends PageFragmentCachingFilter {
	private static final String NAME = WidgetCachingFilter.class.getSimpleName();
	private static final String CACHE_CONFIG_LOCATION = "cacheConfigLocation";
	
	private final Logger logger = LoggerFactory.getLogger(WidgetCachingFilter.class);
	
	private CacheManager cacheManager;
	private Map<String, Set<String>> widgets = new HashMap<String, Set<String>>();
	
	public WidgetCachingFilter() {
	}
	
	@Override
	protected PageInfo buildPageInfo(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain) throws Exception {
		 // Look up the cached page
        final String key = prepareCache(request);
        PageInfo pageInfo = null;
        try {
        	// check clear cache
        	clearCache(request);
        	//
            Element element = blockingCache.get(key);
            if (element == null || element.getObjectValue() == null) {
                try {
                    // Page is not cached - build the response, cache it, and
                    // send to client
                    pageInfo = buildPage(request, response, chain);
                    if (pageInfo.isOk() && pageInfo.getTimeToLiveSeconds()>=0) {
                        blockingCache.put(new Element(key, pageInfo, false, 
                        		(int)pageInfo.getTimeToLiveSeconds(), (int)pageInfo.getTimeToLiveSeconds()));
                        logger.debug("Cache widget {} {} seconds ",
                        		getWidgetConfig(request).getName(), getWidgetConfig(request).getCache());
                    } else {
                    	blockingCache.put(new Element(key, null));
                    }
                } catch (final Throwable throwable) {
                    // Must unlock the cache if the above fails. Will be logged
                    // at Filter
                    blockingCache.put(new Element(key, null));
                    throw new Exception(throwable);
                }
            } else {
                pageInfo = (PageInfo) element.getObjectValue();
            }
        } catch (LockTimeoutException e) {
            // do not release the lock, because you never acquired it
            throw e;
        } finally {
            // all done building page, reset the re-entrant flag
        }
        return pageInfo;
	}
	
	@Override
	protected PageInfo buildPage(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws AlreadyGzippedException, Exception {
		// Invoke the next entity in the chain
        final ByteArrayOutputStream outstr = new ByteArrayOutputStream();
        final GenericResponseWrapper wrapper = new GenericResponseWrapper(response, outstr);
        chain.doFilter(request, wrapper);
        wrapper.flush();
        //
        WidgetConfig widgetConfig = (WidgetConfig) request
				.getAttribute(WidgetConfig.KEY);
        Long timeToLiveSeconds = parseTimeToLiveSeconds(widgetConfig.getCache());
        // Return the page info
        return new PageInfo(wrapper.getStatus(), wrapper.getContentType(), 
                wrapper.getCookies(),
                outstr.toByteArray(), false, timeToLiveSeconds, wrapper.getAllHeaders());
	}
	
	private Long parseTimeToLiveSeconds(String cache) {
		try {
			return NumberUtils.createLong(cache);
		} catch(Exception e){
			return Long.MIN_VALUE;
		}
	}
	
	protected WidgetConfig getWidgetConfig(HttpServletRequest request) {
		return (WidgetConfig) request.getAttribute(WidgetConfig.KEY);
	}
	
	protected String prepareCache(HttpServletRequest request) {
		WidgetConfig widgetConfig =getWidgetConfig(request);
		//
		StringBuffer keyBuffer = new StringBuffer(request.getRequestURI());
		if(request.getQueryString()!=null) {
			keyBuffer.append("?").append(request.getQueryString());
		}
		keyBuffer.append("T").append(widgetConfig.getId());
		Set<String> widgetIds = widgets.get(widgetConfig.getName());
		if(widgetIds==null) {
			widgetIds = new HashSet<String>();
		}
		widgetIds.add(keyBuffer.toString());
		widgets.put(widgetConfig.getName(), widgetIds);
		//
		return keyBuffer.toString();
	}
	
	protected void clearCache(HttpServletRequest request) {
		String widget = (String) request.getSession().getAttribute("clearCache");
		if(widget!=null) {
			Set<String> widgetIds = widgets.get(widget);
			if(widgetIds!=null) {
				for(String widgetId : widgetIds) {
					blockingCache.remove(widgetId);
				}
			}
			request.getSession().removeAttribute("clearCache");
		}
	}
	
	@Override
	protected String calculateKey(HttpServletRequest httpRequest) {
		return null;
	}

	@Override
	protected CacheManager getCacheManager() {
		if(cacheManager==null) {
			String cacheConfigLocation = getFilterConfig()
					.getInitParameter(CACHE_CONFIG_LOCATION);
			URL url = getClass().getResource(cacheConfigLocation);
			cacheManager = new CacheManager(url);
		}
		return cacheManager;
	}
	
	@Override
	protected String getCacheName() {
		String cacheName =  super.getCacheName();
		if(StringUtils.isBlank(cacheName)) {
			cacheName = NAME;
		}
		return cacheName;
	}
	
}
