package fr.ac_versailles.crdp.apiscol.auth.oauth;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.tomcat.util.codec.binary.Base64;

import com.google.gson.Gson;

public class TokenProvider {

	public AccessToken fetchAccessToken(OauthServer oauthServer)
			throws OAuthException {
		HttpURLConnection connection = null;
		DataOutputStream dataOutputStream = null;
		try {
			URL url = oauthServer.getAuthServerUrl();
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			String userCredentials = new StringBuilder()
					.append(oauthServer.getClientId()).append(':')
					.append(oauthServer.getClientSecret()).toString();
			String basicAuth = "Basic "
					+ new String(
							new Base64().encode(userCredentials.getBytes()));
			connection.setRequestProperty("Authorization", basicAuth);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			AccessToken accessToken = null;

			dataOutputStream = new DataOutputStream(
					connection.getOutputStream());
			dataOutputStream.writeBytes("grant_type=client_credentials");
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			String responseStr = response.toString();
			Gson json = new Gson();
			accessToken = json.fromJson(responseStr, AccessToken.class);
			if (accessToken.hasError()) {
				throw new OAuthException(
						"Invalid access token received from url"
								+ oauthServer.getAuthServerUrl().toString()
								+ " with error " + accessToken.getError()
								+ " and message "
								+ accessToken.getErrorDescription());
			}
			return accessToken;

		} catch (Exception e) {
			throw new OAuthException(
					"Impossible to connect to auth server at url"
							+ oauthServer.getAuthServerUrl().toString()
							+ " with message " + e.getMessage());
		} finally {
			if (dataOutputStream != null) {
				try {
					dataOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
