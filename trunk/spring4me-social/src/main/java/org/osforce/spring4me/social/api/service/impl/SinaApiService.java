/**
 *
 */
package org.osforce.spring4me.social.api.service.impl;

import java.util.Map;

import org.osforce.spring4me.social.api.oauth.SinaApi;
import org.osforce.spring4me.social.api.service.AbstractApiService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:53:19 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class SinaApiService extends AbstractApiService {

	private static final String STATUSES_UPDATE = "http://api.t.sina.com.cn/statuses/update";

	private String apiKey;
	private String apiSecret;

	public SinaApiService(String apiKey, String apiSecret) {
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	}

	public OAuthService getOAuthService() {
		return new ServiceBuilder()
				.provider(SinaApi.class)
				.apiKey(apiKey)
				.apiSecret(apiSecret)
				.build();
	}

	public OAuthService getOAuthService(String callback) {
		return new ServiceBuilder()
				.provider(SinaApi.class)
				.apiKey(apiKey)
				.apiSecret(apiSecret)
				.callback(callback)
				.build();
	}

	@Override
	protected OAuthRequest getStatusUpdateRequest(Map<String, Object> params) {
		OAuthRequest request = new OAuthRequest(Verb.POST, STATUSES_UPDATE
				+ "." + (String) params.get("format"));
		request.addBodyParameter("status", (String) params.get("status"));
		return request;
	}

}
