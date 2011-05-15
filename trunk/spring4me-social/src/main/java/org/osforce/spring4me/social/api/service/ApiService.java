/**
 *
 */
package org.osforce.spring4me.social.api.service;

import java.util.Map;

import org.scribe.oauth.OAuthService;

/**
 * @author gavin
 * @since 1.0.3
 * @create May 10, 2011 - 3:20:50 PM
 *  <a href="http://www.opensourceforce.org">开源力量</a>
 */
public interface ApiService {

	OAuthService getOAuthService();

	OAuthService getOAuthService(String callback);

	String updateStatus(Map<String, Object> params);
}
