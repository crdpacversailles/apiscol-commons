package fr.ac_versailles.crdp.apiscol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.naming.NamingContext;

import fr.ac_versailles.crdp.apiscol.auth.oauth.OauthServersProxy;
import fr.ac_versailles.crdp.apiscol.transactions.KeyLock;
import fr.ac_versailles.crdp.apiscol.transactions.KeyLockManager;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;
import fr.ac_versailles.crdp.apiscol.utils.PropsUtils;

public class ApiscolApi {
	private static Properties properties;
	private static Logger logger;
	protected static String version;
	private static NamingContext initialContextContainer;
	protected KeyLockManager keyLockManager;
	private HashMap<String, String> dbConnexionParameters;
	private HashMap<String, String> oauthConnexionParameters;
	private ServletContext context;
	private URI externalUri;
	protected OauthServersProxy oauthServersProxy;

	public ApiscolApi(@Context ServletContext context) {
		this.context = context;
		createLogger();
		createKeyLockManager();
		loadProperties(context);
		LogUtility.setLoglevel(Level.toLevel(properties
				.getProperty(ParametersKeys.logLevel.toString())));
		readServiceExternalUriFromProperties();
	}

	private void readServiceExternalUriFromProperties() {
		String externalUriString = getProperty(ParametersKeys.service_wan_url,
				context);
		if (StringUtils.isEmpty(externalUriString)) {
			getLogger()
					.error("Please provide service external uri in service poperties file or in host context.xml");
		}
		try {
			externalUri = new URI(externalUriString);
		} catch (URISyntaxException e) {
			getLogger().error(
					"Please provide a valid uri for key external_uri in place of : "
							+ externalUriString);
		}

	}

	private static void createLogger() {
		if (logger == null)
			logger = LogUtility.createLogger(ApiscolApi.class
					.getCanonicalName());
	}

	private void createKeyLockManager() {
		keyLockManager = KeyLockManager.getInstance();
	}

	protected void takeAndReleaseGlobalLock() {
		KeyLock keyLock = null;
		try {
			keyLock = keyLockManager.getLock(KeyLockManager.GLOBAL_LOCK_KEY);
			keyLock.lock();
			try {
				getLogger()
						.info(String
								.format("Passing through mutual exclusion for all the content service"));
			} finally {
				keyLock.unlock();

			}
		} finally {
			if (keyLock != null) {
				keyLock.release();
			}
		}
	}

	protected static void loadProperties(ServletContext context) {
		if (properties == null) {
			properties = PropsUtils.loadProperties(context);
		}
		if (properties == null) {
			getLogger().error("Properties file whas not correctly loaded !!");

		} else {
			version = properties.getProperty(ParametersKeys.apiscolVersion
					.toString());
		}

		if (StringUtils.isEmpty(version)) {
			getLogger()
					.error("There is no version number in this apiscol web service, version will be 0.0.0 by default");
			version = "0.0.0";
			return;
		}
		Pattern p = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");
		Matcher m = p.matcher(version);
		if (!m.matches())
			getLogger().error(
					"Version number " + version
							+ " does not have the standard pattern");
	}

	public static String getProperty(ParametersKeys key, ServletContext context) {
		return getPropertyByStringKey(key.toString(), context);
	}

	public static String getPropertyByStringKey(String key,
			ServletContext context) {
		String property = getPropertyFromInitialContextByStringKey(key);
		if (property != null) {
			getLogger().debug(
					"The following property was overriden by host initial xml context : "
							+ key);
			return property;
		} else
			getLogger().debug(
					"The following property was not found in  host initial xml context : "
							+ key.toString());

		loadProperties(context);

		if (!properties.containsKey(key)) {
			getLogger()
					.error("The configuration files of this web service does not contain any value for parameter : "
							+ key.toString());
		}
		return properties.getProperty(key);
	}

	public static String getPropertyFromInitialContext(final ParametersKeys key) {
		if (key == null)
			return null;
		return getPropertyFromInitialContextByStringKey(key.toString());

	}

	public static String getPropertyFromInitialContextByStringKey(
			final String key) {

		if (key == null || key.isEmpty())
			return null;

		try {
			final Object object = getHostContext().lookup(key);
			if (object != null)
				return object.toString();
		} catch (Exception e) {
			getLogger().debug(e.getMessage());
			return null;
		}
		return null;

	}

	private static NamingContext getHostContext() throws NamingException {
		if (initialContextContainer == null)
			initialContextContainer = (NamingContext) new InitialContext()
					.lookup("java:comp/env");
		return initialContextContainer;
	}

	protected String guessRequestedFormat(HttpServletRequest request,
			String format) {
		// TODO set default format
		if (format == null)
			return RequestHandler.extractAcceptHeader(request);
		else
			return RequestHandler.convertFormatQueryParam(format);
	}

	protected void initializeDbConnexionParameters() {
		dbConnexionParameters = new HashMap<String, String>();
		dbConnexionParameters.put(ParametersKeys.dbHosts.toString(),
				getProperty(ParametersKeys.dbHosts, context));
		dbConnexionParameters.put(ParametersKeys.dbPorts.toString(),
				getProperty(ParametersKeys.dbPorts, context));
	}

	protected void initializeOAuthConnexionParameters() {
		oauthConnexionParameters = new HashMap<String, String>();
		oauthConnexionParameters
				.put(ParametersKeys.oAuthAuthorizationServerUri.toString(),
						getProperty(ParametersKeys.oAuthAuthorizationServerUri,
								context));
		oauthConnexionParameters.put(ParametersKeys.oAuthClientId.toString(),
				getProperty(ParametersKeys.oAuthClientId, context));
		oauthConnexionParameters.put(
				ParametersKeys.oAuthClientSecret.toString(),
				getProperty(ParametersKeys.oAuthClientSecret, context));
	}

	protected HashMap<String, String> getDbConnexionParameters() {
		if (dbConnexionParameters == null
				|| !dbConnexionParameters.containsKey(ParametersKeys.dbHosts)
				|| !dbConnexionParameters.containsKey(ParametersKeys.dbPorts))
			initializeDbConnexionParameters();
		return dbConnexionParameters;
	}

	protected URI getExternalUri() {
		return externalUri;
	}

	protected void fetchOauthServersProxy(ServletContext context) {
		oauthServersProxy = (OauthServersProxy) context
				.getAttribute(OauthServersProxy.ENVIRONMENT_PARAMETER_KEY);
		if (!(oauthServersProxy instanceof OauthServersProxy)) {
			getLogger()
					.error("Impossible to fetch instance of OauthServersProxy from servlet context");
		}
		getLogger()
				.info("Successfully feteched instance of OauthServersProxy from servlet context");
	}

	public static Logger getLogger() {
		if (logger == null) {
			createLogger();
		}
		return logger;
	}
}
