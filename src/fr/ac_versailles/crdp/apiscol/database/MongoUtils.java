package fr.ac_versailles.crdp.apiscol.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

import fr.ac_versailles.crdp.apiscol.ParametersKeys;
import fr.ac_versailles.crdp.apiscol.utils.LogUtility;

public class MongoUtils {
	private static Logger logger;
	public static final CharSequence MULTIVALUED_PARAMETERS_SEPARATOR = ",";

	public static Mongo getMongoConnection(Map<String, String> dbParams)
			throws DBAccessException {

		Mongo mongo;
		ConnexionParameters parameters = new ConnexionParameters(dbParams);

		try {
			getLogger().info("Creating a new Mongo instance");
			if (parameters.isSingle())
				mongo = new MongoClient(parameters.getServerAdresses().get(0));
			else
				mongo = new MongoClient(parameters.getServerAdresses());
			mongo.setWriteConcern(WriteConcern.SAFE);
		} catch (MongoException e) {
			String message = "Problem while connecting to mongodb ";
			getLogger().error(message);
			throw new DBAccessException(message + e.getMessage());
		}
		return mongo;
	}

	public static DBCollection getCollection(String dbName,
			String collectionName, Mongo mongo) throws DBAccessException {
		DB apiscolDB;
		try {
			apiscolDB = mongo.getDB(dbName);
		} catch (MongoException e) {
			String message = "Problem while accessing to database " + dbName;
			getLogger().error(message);
			throw new DBAccessException(message + e.getMessage());
		}
		DBCollection resourcesCollection;
		try {
			resourcesCollection = apiscolDB.getCollection(collectionName);
		} catch (MongoException e) {
			String message = "Problem while accessing collection "
					+ collectionName;
			getLogger().error(message);
			throw new DBAccessException(message + e.getMessage());
		}
		return resourcesCollection;
	}

	public static void dbDisconnect(Mongo mongo) {
		if (mongo != null) {
			mongo.close();
		}
	}

	private static void createLogger() {
		logger = LogUtility.createLogger(MongoUtils.class.getCanonicalName());

	}

	public static Logger getLogger() {
		if (logger == null)
			createLogger();
		return logger;
	}

	private static class ConnexionParameters {
		private boolean isSingle;

		private List<ServerAddress> serverAdresses;

		public ConnexionParameters(Map<String, String> dbParams)
				throws DBAccessException {
			if (!dbParams.containsKey(ParametersKeys.dbHosts.toString()))
				throw new DBAccessException(
						"Please provide mongodb host(s) under the key "
								+ ParametersKeys.dbHosts.toString());
			String dbHosts = dbParams.get(ParametersKeys.dbHosts.toString());
			if (!dbParams.containsKey(ParametersKeys.dbPorts.toString()))
				throw new DBAccessException(
						"Please provide mongodb ports(s) under the key "
								+ ParametersKeys.dbPorts.toString());
			String dbPorts = dbParams.get(ParametersKeys.dbPorts.toString());
			String[] dbHostsArray;
			Integer[] dbPortsArray;
			if (dbHosts.contains(MongoUtils.MULTIVALUED_PARAMETERS_SEPARATOR)) {
				this.setSingle(false);
				dbHostsArray = dbHosts
						.split((String) MongoUtils.MULTIVALUED_PARAMETERS_SEPARATOR);
				String[] dbPortsStringArray = dbHosts
						.split((String) MongoUtils.MULTIVALUED_PARAMETERS_SEPARATOR);
				dbPortsArray = new Integer[dbPortsStringArray.length];
				if (dbHostsArray.length > dbPortsStringArray.length)
					throw new DBAccessException(
							"Please provide a port number for each mongodb host ");

				for (int i = 0; i < dbPortsStringArray.length; i++) {
					String port = dbPortsStringArray[i];
					if (!StringUtils.isNumeric(port))
						throw new DBAccessException(
								"One of the provided port number is not numeric : "
										+ port);

				}

			} else {
				this.setSingle(true);
				dbHostsArray = new String[1];
				dbPortsArray = new Integer[1];
				dbHostsArray[0] = dbHosts;
				if (!StringUtils.isNumeric(dbPorts))
					throw new DBAccessException(
							"The provided port number is not numeric : "
									+ dbPorts);
				dbPortsArray[0] = Integer.parseInt(dbPorts);
			}
			this.serverAdresses = new ArrayList<ServerAddress>();
			for (int i = 0; i < dbHostsArray.length; i++) {
				try {
					this.serverAdresses.add(new ServerAddress(dbHostsArray[i],
							dbPortsArray[i]));
				} catch (UnknownHostException e) {
					throw new DBAccessException(
							"Impossible to build server address from provided parameters : "
									+ e.getMessage());
				}

			}
		}

		public List<ServerAddress> getServerAdresses() {
			return this.serverAdresses;
		}

		public boolean isSingle() {
			return isSingle;
		}

		private void setSingle(boolean isSingle) {
			this.isSingle = isSingle;
		}
	}
}
