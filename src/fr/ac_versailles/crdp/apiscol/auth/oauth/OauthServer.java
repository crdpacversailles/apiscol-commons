package fr.ac_versailles.crdp.apiscol.auth.oauth;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class OauthServer {

	private URL authServerUrl;
	private String clientId;
	private String clientSecret;
	private List<String> protectedResourcesFqdn;

	public OauthServer(URL authServerUrl, String clientId, String clientSecret,
			List<String> protectedResourcesFqdn) {
		this.setAuthServerUrl(authServerUrl);
		this.setClientId(clientId);
		this.setClientSecret(clientSecret);
		this.protectedResourcesFqdn = protectedResourcesFqdn;

	}

	public URL getAuthServerUrl() {
		return authServerUrl;
	}

	public void setAuthServerUrl(URL authServerUrl) {
		this.authServerUrl = authServerUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public boolean protectsResource(String url) {
		Iterator<String> it = protectedResourcesFqdn.iterator();
		while (it.hasNext()) {
			String protectedresoureFqdn = (String) it.next();
			if (StringUtils.startsWith(url, protectedresoureFqdn)) {
				return true;
			}
		}
		return false;
	}

}
