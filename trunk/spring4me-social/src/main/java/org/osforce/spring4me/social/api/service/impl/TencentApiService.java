/**
 *
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
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:53:30 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
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
