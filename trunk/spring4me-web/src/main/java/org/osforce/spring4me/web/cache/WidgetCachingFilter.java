package org.osforce.spring4me.web.cache;

import java.io.ByteArrayOutputStream;
import java.net.URL;

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
import org.osforce.spring4me.web.widget.WidgetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 21, 2011 - 8:24:02 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class WidgetCachingFilter extends PageFragmentCachingFilter {
	private static final String NAME = WidgetCachingFilter.class.getSimpleName();
	private final Logger logger = LoggerFactory.getLogger(WidgetCachingFilter.class);
	
	private CacheManager cacheManager;
	
	public WidgetCachingFilter() {
	}
	
	@Override
	protected PageInfo buildPageInfo(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain) throws Exception {
		 // Look up the cached page
        final String key = calculateKey(request);
        PageInfo pageInfo = null;
        try {
            Element element = blockingCache.get(key);
            if (element == null || element.getObjectValue() == null) {
                try {
                    // Page is not cached - build the response, cache it, and
                    // send to client
                    pageInfo = buildPage(request, response, chain);
                    if (pageInfo.isOk()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("PageInfo ok. Adding to cache "
                                    + blockingCache.getName() + " with key "
                                    + key);
                        }
                        blockingCache.put(new Element(key, pageInfo, false, 
                        		(int)pageInfo.getTimeToLiveSeconds(), (int)pageInfo.getTimeToLiveSeconds()));
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("PageInfo was not ok(200). Putting null into cache "
                                    + blockingCache.getName()
                                    + " with key "
                                    + key);
                        }
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
        if(timeToLiveSeconds==null) {
        	timeToLiveSeconds = blockingCache.getCacheConfiguration().getTimeToLiveSeconds();
        }
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
	
	@Override
	protected String calculateKey(HttpServletRequest request) {
		WidgetConfig widgetConfig = (WidgetConfig) request
				.getAttribute(WidgetConfig.KEY);
		return widgetConfig.getId();
	}

	@Override
	protected CacheManager getCacheManager() {
		if(cacheManager==null) {
			URL url = getClass().getResource("/ehcache-widget.xml");
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
