/*
 * Copyright 2011-2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osforce.spring4me.social.api.oauth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

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