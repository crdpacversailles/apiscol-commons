package fr.ac_versailles.crdp.apiscol.auth.oauth;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import fr.ac_versailles.crdp.apiscol.ApiscolApi;
import fr.ac_versailles.crdp.apiscol.ParametersKeys;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;

public class OAuthInitializer implements ServletContextListener {

	private static final String SERVER_COUNT_PLACEHOLDER = "X";
	private Logger logger;

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		createLogger();
		OauthServersProxy oAuthServersProxy = new OauthServersProxy();
		ServletContext context = servletContextEvent.getServletContext();
		int oauthServerCounter = 0;
		while (true) {
			oauthServerCounter++;
			String serverUriKey = ParametersKeys.oAuthAuthorizationServerUri
					.toString();
			serverUriKey = this.replaceServerCountPlaceholder(serverUriKey,
					oauthServerCounter);
			String oauthServerUrlString = ApiscolApi.getPropertyByStringKey(
					serverUriKey, context);

			if (oauthServerUrlString == null || oauthServerUrlString.isEmpty()) {
				logger.info("No more oauth server parameters fetched from configuration");
				break;
			}
			URL oauthServerUrl;
			try {
				oauthServerUrl = new URL(oauthServerUrlString);
			} catch (MalformedURLException e) {
				logger.error("Invalid url provided :" + oauthServerUrlString);
				continue;
			}

			logger.info("Looking for oauth server parameters for server n°."
					+ oauthServerCounter);

			logger.info("Oauth uri : " + oauthServerUrlString);
			String clientIdKey = ParametersKeys.oAuthClientId.toString();
			String clientSecretKey = ParametersKeys.oAuthClientSecret
					.toString();
			String serverResourcesFqdnKey = ParametersKeys.oAuthServerResourcesFqdn
					.toString();
			clientIdKey = replaceServerCountPlaceholder(clientIdKey,
					oauthServerCounter);
			clientSecretKey = replaceServerCountPlaceholder(clientSecretKey,
					oauthServerCounter);
			serverResourcesFqdnKey = replaceServerCountPlaceholder(
					serverResourcesFqdnKey, oauthServerCounter);
			String oauthServerClientId = ApiscolApi.getPropertyByStringKey(
					clientIdKey, context);
			if (StringUtils.isBlank(oauthServerClientId)) {
				logger.error("Missing parameter : client_id for server n°"
						+ oauthServerCounter);
				continue;
			}
			String oauthServerClientSecret = ApiscolApi.getPropertyByStringKey(
					clientSecretKey, context);
			if (StringUtils.isBlank(oauthServerClientSecret)) {
				logger.error("Missing parameter : client_secret for server n°"
						+ oauthServerCounter);
				continue;
			}
			String oauthServerProtectedResourcesFqdn = ApiscolApi
					.getPropertyByStringKey(serverResourcesFqdnKey, context);
			if (StringUtils.isBlank(oauthServerProtectedResourcesFqdn)) {
				logger.error("Missing parameter : client_secret for server n°"
						+ oauthServerCounter);
				continue;
			}
			String[] oauthServerProtectedResourcesFqdnList = oauthServerProtectedResourcesFqdn
					.split(",");
			if (oauthServerProtectedResourcesFqdnList.length == 0) {
				logger.error("Void list of protected resources fqdn for server n°"
						+ oauthServerCounter);
				continue;
			}
			OauthServerBuilder oauthServerBuilder = new OauthServerBuilder();

			oauthServerBuilder.setAuthServerUrl(oauthServerUrl)
					.setClientId(oauthServerClientId)
					.setClientSecret(oauthServerClientSecret);
			for (int i = 0; i < oauthServerProtectedResourcesFqdnList.length; i++) {
				oauthServerBuilder
						.addProtectedResourceFqdn(oauthServerProtectedResourcesFqdnList[i]);

			}
			OauthServer oAuthServer = oauthServerBuilder.build();
			oAuthServersProxy.addOAuthServer(oAuthServer);
		}
		context.setAttribute(OauthServersProxy.ENVIRONMENT_PARAMETER_KEY,
				oAuthServersProxy);
	}

	private String replaceServerCountPlaceholder(String key,
			int oauthServerCounter) {
		return key.replace(SERVER_COUNT_PLACEHOLDER,
				Integer.toString(oauthServerCounter));
	}

	private void createLogger() {
		if (logger == null)
			logger = LogUtility
					.createLogger(this.getClass().getCanonicalName());
	}

}
