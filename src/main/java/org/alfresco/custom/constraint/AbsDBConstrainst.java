/**
 * This file is part of Alfresco Custom Database Constraint Extension.
 *
 * Alfresco Custom Database Constraint Extension is free software: you 
 * can redistribute it and/or modify it under the terms of the GNU LESSER 
 * GENERAL PUBLIC LICENSE as published by the Free Software Foundation, 
 * either version 3 of the License, or any later version.
 *
 * Alfresco Custom Database Constraint Extension is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the 
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco Custom Database Constraint Extension. If not, 
 * see <http://www.gnu.org/licenses/>. 
 */
package org.alfresco.custom.constraint;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.alfresco.custom.constraint.db.Database;
import org.alfresco.custom.constraint.db.DatabaseImpl;
import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;
import org.alfresco.service.cmr.dictionary.DictionaryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract Alfresco Database Constraint
 * 
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @since 1.0
 * @version 1.0
 */
public abstract class AbsDBConstrainst extends ListOfValuesConstraint {
	private static Log logger = LogFactory.getLog(AbsDBConstrainst.class);

	private final static String NO_INTEGER_DATA = "No Integer data";
	private final static String NO_LONG_DATA = "No Long data";

	private final static String CUSTOM_DATABASE_CONSTRAINT_NAME = "DBLIST";
	public final static String URL = "url";
	public final static String DRIVER = "driver";
	public final static String USER = "user";
	public final static String PWD = "pwd";
	public final static String QUERY = "query";
	public final static String RELOAD = "reload";
	public final static String POSITION = "position";

	/**
	 * Alfresco properties reader
	 */
	private Properties properties;

	/**
	 * Store the last constraint update time
	 */
	private long lastUpdateTime = 0;

	/**
	 * <b>No required value</b>
	 * 
	 * The URL default value is the alfresco database string connection mapped
	 * at alfresco-global.properties file as <b>db.url</b> key.
	 */
	private String url = null;

	/**
	 * <b>No required value</b>
	 * 
	 * The database driver default value is the alfresco database driver, mapped
	 * at alfresco-global.properties file as <b>db.driver</b> key.
	 */
	private String driver = null;

	/**
	 * <b>No required value</b>
	 * 
	 * The database user default value is the alfresco database user, mapped at
	 * alfresco-global.properties file as <b>db.username</b> key.
	 */
	private String user = null;

	/**
	 * <b>No required value</b>
	 * 
	 * The database password default value is the alfresco database password,
	 * mapped at alfresco-global.properties file as <b>db.password</b> key.
	 */
	private String pwd = null;

	/**
	 * <b>Parameter required</b>
	 * 
	 * This is the query used to get the list of constraint.
	 */
	private String query = null;

	/**
	 * <b>No required parameter</b>
	 * 
	 * the database return column. 1 is the first and default value.
	 */
	private String position = "1";

	/**
	 * <b>No required parameter</b>
	 * 
	 * The constraint reload time (in mile-seconds). If no value, the time to
	 * constraint reload will be 300000 mile-seconds (5 minutes).
	 */
	private String reload = "3000000";

	/**
	 * @see org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint#getType()
	 */
	@Override
	public String getType() {
		return CUSTOM_DATABASE_CONSTRAINT_NAME;
	}

	/**
	 * @see org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.getClass().getSimpleName());
		sb.append("[ ");
		sb.append(DRIVER).append("=").append(this.driver).append(",");
		sb.append(URL).append("=").append(this.url).append(",");
		sb.append(USER).append("=").append(this.user).append(",");
		sb.append(PWD).append("=").append(this.pwd).append(",");
		sb.append(QUERY).append("=").append(this.query).append(",");
		sb.append(RELOAD).append("=").append(this.reload).append(",");
		sb.append(POSITION).append("=").append(this.position);
		sb.append(" ]");

		return sb.toString();
	}

	/**
	 * @see org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint#getParameters()
	 */
	@Override
	public Map<String, Object> getParameters() {
		Map<String, Object> params = new HashMap<String, Object>(2);

		params.put(DRIVER, this.driver);
		params.put(URL, this.url);
		params.put(USER, this.user);
		params.put(PWD, this.pwd);
		params.put(QUERY, this.query);
		params.put(RELOAD, this.reload);
		params.put(POSITION, this.position);

		return params;
	}

	/**
	 * @see org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint#initialize()
	 */
	@Override
	public void initialize() {
		// set default parameter when necessary
		this.setDefaultParameters();

		checkPropertyNotNull(QUERY, this.query);
		checkPropertyIsInteger(this.position);
		checkPropertyIsLong(this.reload);

		super.setAllowedValues(loadDB());
	}

	/**
	 * @see org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint#getAllowedValues()
	 */
	@Override
	public List<String> getAllowedValues() {

		long timeAccess = System.currentTimeMillis();
		long timeReload = Long.parseLong(this.reload);
		long timeDif = timeAccess - (this.lastUpdateTime + timeReload);

		if (super.getAllowedValues() == null || super.getAllowedValues().isEmpty() || timeDif > 0) {
			super.setAllowedValues(loadDB());
			lastUpdateTime = System.currentTimeMillis();

			logger.debug("Update Allowed Values. [ numberOfElements=" + super.getAllowedValues().size() + ", timeToUpdate=" + lastUpdateTime + "]");
		}

		return super.getAllowedValues();
	}

	/**
	 * Check if the parameter is a integer value
	 * 
	 * @param value
	 */
	private void checkPropertyIsInteger(String value) {
		try {
			logger.debug("Checking if the value is integer ");
			Integer.parseInt(value);
		} catch (Exception e) {
			throw new DictionaryException(NO_INTEGER_DATA, CUSTOM_DATABASE_CONSTRAINT_NAME, getShortName());
		}
	}

	/**
	 * Check if the parameter is a integer value
	 * 
	 * @param value
	 */
	private void checkPropertyIsLong(String value) {
		try {
			logger.debug("Checking if the value is long");
			Long.parseLong(value);
		} catch (Exception e) {
			throw new DictionaryException(NO_LONG_DATA, CUSTOM_DATABASE_CONSTRAINT_NAME, getShortName());
		}
	}

	/**
	 * Set the default parameters when necessary
	 */
	private void setDefaultParameters() {

		if (properties == null) {
			logger.debug("Load the properties data");
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream in = loader.getResourceAsStream("alfresco-global.properties");
			properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// set database driver
		if (driver == null || driver.trim().isEmpty()) {
			driver = properties.getProperty("db.driver");
		}

		// set database user
		if (user == null || user.trim().isEmpty()) {
			user = properties.getProperty("db.username");
		}

		// set database password
		if (pwd == null || pwd.trim().isEmpty()) {
			pwd = properties.getProperty("db.password");
		}

		// set database URL
		if (url == null || url.trim().isEmpty()) {
			url = properties.getProperty("db.url");

			if (url.contains("${db.name}")) {
				String dbName = properties.getProperty("db.name");
				url = url.replace("${db.name}", dbName);
			}
		}

	}

	/**
	 * Load data from database
	 * 
	 * @return {@link List} of {@link String} - a list of Strings candidate to
	 *         be a constraint
	 */
	protected List<String> loadDB() {
		Database database = new DatabaseImpl();
		database.setDatabaseDriver(driver);
		database.setConnectionUrl(url);
		database.setUser(user);
		database.setPwd(pwd);

		return database.executeQuery(this.query);
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param pwd
	 *            the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the reload
	 */
	public String getReload() {
		return reload;
	}

	/**
	 * @param reload
	 *            the reload to set
	 */
	public void setReload(String reload) {
		this.reload = reload;
	}

}
