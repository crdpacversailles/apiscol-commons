package fr.ac_versailles.crdp.apiscol.auth.oauth;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OauthServerBuilder {

	private URL authServerUrl;

	private String clientId;

	private String clientSecret;

	private List<String> protectedResourcesFqdn = new ArrayList<String>();

	public URL getAuthServerUrl() {
		return authServerUrl;
	}

	public OauthServerBuilder setAuthServerUrl(URL authServerUrl) {
		this.authServerUrl = authServerUrl;
		return this;
	}

	public String getClientId() {
		return clientId;

	}

	public String getClientSecret() {
		return clientSecret;
	}

	public OauthServerBuilder setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
		return this;
	}

	public OauthServerBuilder setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public OauthServerBuilder addProtectedResourceFqdn(
			String protectedResourceFqdn) {
		getProtectedResourcesFqdn().add(protectedResourceFqdn);
		return this;
	}

	public OauthServer build() {
		return new OauthServer(getAuthServerUrl(), getClientId(),
				getClientSecret(), getProtectedResourcesFqdn());
	}

	public List<String> getProtectedResourcesFqdn() {
		return protectedResourcesFqdn;
	}

}
