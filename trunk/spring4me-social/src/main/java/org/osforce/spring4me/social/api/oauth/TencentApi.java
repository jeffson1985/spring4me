/**
 *
 */
package org.osforce.spring4me.social.api.oauth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:53:10 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class TencentApi extends DefaultApi10a {

	private static final String AUTHORIZE_URL = "https://open.t.qq.com/cgi-bin/authorize?oauth_token=%s&oauth_callback=%s";

	private OAuthConfig oAuthConfig;

	public TencentApi() {
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "https://open.t.qq.com/cgi-bin/access_token";
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken(), oAuthConfig.getCallback());
	}

	@Override
	public String getRequestTokenEndpoint() {
		return "https://open.t.qq.com/cgi-bin/request_token";
	}

	@Override
	public Verb getRequestTokenVerb() {
		return Verb.GET;
	}

	@Override
	public OAuthService createService(OAuthConfig config) {
		this.oAuthConfig = config;
		return super.createService(config);
	}

}
