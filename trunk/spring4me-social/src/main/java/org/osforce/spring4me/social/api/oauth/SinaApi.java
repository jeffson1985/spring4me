package org.osforce.spring4me.social.api.oauth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class SinaApi extends DefaultApi10a {

	private static final String AUTHORIZE_URL = "http://api.t.sina.com.cn/oauth/authorize?oauth_token=%s&oauth_callback=%s";

	private OAuthConfig oAuthConfig;

	public SinaApi() {
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "http://api.t.sina.com.cn/oauth/access_token";
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {

		return String.format(AUTHORIZE_URL, requestToken.getToken(), oAuthConfig.getCallback());
	}

	@Override
	public String getRequestTokenEndpoint() {
		return "http://api.t.sina.com.cn/oauth/request_token";
	}

	@Override
	public OAuthService createService(OAuthConfig config) {
		this.oAuthConfig = config;
		return super.createService(config);
	}

}
