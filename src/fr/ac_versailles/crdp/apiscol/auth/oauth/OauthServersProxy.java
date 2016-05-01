package fr.ac_versailles.crdp.apiscol.auth.oauth;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OauthServersProxy {

	public static final String ENVIRONMENT_PARAMETER_KEY = "oauth-servers-proxy";

	private static final float DEFAULT_REMAINING_SECONDS_REQUIREMENT = 5.0f;

	List<OauthServer> oAuthServers = new LinkedList<OauthServer>();
	Map<String, AccessToken> storedAccessTokens = new HashMap<String, AccessToken>();

	private TokenProvider tokenProvider;

	public String getAccesTokenForUrl(String url) throws OAuthException {
		return this.getAccesTokenForUrl(url,
				DEFAULT_REMAINING_SECONDS_REQUIREMENT);
	}

	public String getAccesTokenForUrl(String url,
			float remainingSecondsRequirement) throws OAuthException {
		Iterator<OauthServer> oAuthServersIterator = oAuthServers.iterator();
		while (oAuthServersIterator.hasNext()) {
			OauthServer oauthServer = (OauthServer) oAuthServersIterator.next();
			if (oauthServer.protectsResource(url)) {
				URL oAuthServerUrl = oauthServer.getAuthServerUrl();
				AccessToken accessToken = null;
				if (storedAccessTokens.containsKey(oAuthServerUrl.toString())) {
					accessToken = storedAccessTokens.get(oAuthServerUrl
							.toString());
				}
				if (accessToken == null
						|| accessToken.getRemainingSeconds() < remainingSecondsRequirement) {
					if (tokenProvider == null) {
						tokenProvider = new TokenProvider();
					}
					accessToken = tokenProvider.fetchAccessToken(oauthServer);
				}
				if (accessToken == null) {
					return null;
				}
				storedAccessTokens.put(oAuthServerUrl.toString(), accessToken);
				return accessToken.getAccessToken();
			}
		}
		return null;

	}

	public void addOAuthServer(OauthServer oAuthServer) {
		oAuthServers.add(oAuthServer);

	}

}
