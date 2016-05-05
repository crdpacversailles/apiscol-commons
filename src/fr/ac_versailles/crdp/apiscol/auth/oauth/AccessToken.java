package fr.ac_versailles.crdp.apiscol.auth.oauth;

import org.apache.commons.lang.StringUtils;

public class AccessToken {
	private String access_token;
	private float expires_in = 0;
	private String error;
	private String error_description;
	private long creationTime;

	public AccessToken() {
		creationTime = System.currentTimeMillis() / 1000L;
	}

	public String getAccessToken() {
		return access_token;
	}

	public float getExpiresIn() {
		return expires_in;
	}

	public String getError() {
		return error;
	}

	public boolean hasError() {
		return StringUtils.isNotBlank(error);
	}

	public String getErrorDescription() {
		return error_description;
	}


	public float getRemainingSeconds() {
		long spentTime = System.currentTimeMillis() / 1000L-creationTime;
		return Math.max(0, expires_in-spentTime);
	}

}
