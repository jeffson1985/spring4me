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

package org.osforce.spring4me.social.api.service.impl;

import java.util.Map;

import org.osforce.spring4me.social.api.oauth.TencentApi;
import org.osforce.spring4me.social.api.service.AbstractApiService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.SignatureType;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-3-14 - ����4:51:24
 */
public class TencentApiService extends AbstractApiService {

	private static final String STATUSES_UPDATE = "http://open.t.qq.com/api/t/add";

	private String apiKey;
	private String apiSecret;

	private String clientip;

	public TencentApiService(String apiKey, String apiSecret) {
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}

	public OAuthService getOAuthService() {
		return new ServiceBuilder()
				.provider(TencentApi.class)
				.apiKey(apiKey)
				.apiSecret(apiSecret)
				.signatureType(SignatureType.QueryString)
				.build();
	}

	public OAuthService getOAuthService(String callback) {
		return new ServiceBuilder()
					.provider(TencentApi.class)
					.apiKey(apiKey)
					.apiSecret(apiSecret)
					.callback(callback)
					.signatureType(SignatureType.QueryString)
					.build();
	}

	@Override
	protected OAuthRequest getStatusUpdateRequest(Map<String, Object> params) {
		OAuthRequest request = new OAuthRequest(Verb.POST, STATUSES_UPDATE);
		request.addBodyParameter("content", (String) params.get("status"));
		request.addBodyParameter("format", (String) params.get("format"));
		request.addBodyParameter("clientip", clientip);
		return request;
	}

}