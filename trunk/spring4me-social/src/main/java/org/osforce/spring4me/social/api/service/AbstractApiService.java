/**
 *
 */
package org.osforce.spring4me.social.api.service;

import java.util.Map;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;

/**
 * @author gavin
 * @since 1.0.3
 * @create May 10, 2011 - 4:24:35 PM
 *  <a href="http://www.opensourceforce.org">开源力量</a>
 */
public abstract class AbstractApiService implements ApiService {

	public String updateStatus(Map<String, Object> params) {
		OAuthRequest request = getStatusUpdateRequest(params);
		Token accessToken = (Token) params.get("accessToken");
		getOAuthService().signRequest(accessToken, request);
		Response response = request.send();
		return response.getBody();
	}

	protected abstract OAuthRequest getStatusUpdateRequest(Map<String, Object> params);

}
