/**
 *
 */
package org.osforce.spring4me.social.api.service;

import java.util.Map;

import org.scribe.oauth.OAuthService;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:53:42 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public interface ApiService {

	OAuthService getOAuthService();

	OAuthService getOAuthService(String callback);

	String updateStatus(Map<String, Object> params);
}
