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
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Message Controller to Alfresco DBConstraint
 * 
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @since 1.0
 * @version 1.0
 */
public class Msg {

	public static final String ERROR_LOAD_DRIVER = "dbconstraint.error.driver.load";
	public static final String ERROR_OPEN_CONNECTION = "dbconstraint.error.connection.open";
	public static final String ERROR_CLOSE_CONNECTION = "dbconstraint.error.connection.close";
	public static final String ERROR_EXECUTE_QUERY = "dbconstraint.error.query.execute";
	public static final String ERROR_UPDATE_ALLOWED_VALUES = "dbconstraint.error.allowedvalues.update";

	public static final String SUCCESS_LOAD_DRIVER = "dbconstraint.success.driver.load";
	public static final String SUCCESS_OPEN_CONNECTION = "dbconstraint.success.connection.open";
	public static final String SUCCESS_CLOSE_CONNECTION = "dbconstraint.success.connection.close";
	public static final String SUCCESS_EXECUTE_QUERY = "dbconstraint.success.query.execute";
	public static final String SUCCESS_UPDATE_ALLOWED_VALUES = "dbconstraint.success.allowedvalues.update";

	private Properties prop = null;
	private static Msg message = null;

	private Msg() {
	};

	/**
	 * Get Message Instance
	 * 
	 * @return {@link Msg}
	 */
	public static Msg getInstance() {
		if (message == null) {
			message = new Msg();
			message.prop = new Properties();
			message.loadBundle();
		}

		return message;
	}

	/**
	 * load bundle file
	 */
	private void loadBundle() {
		InputStream inputStream;
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream("message.properties");
			message.prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the message value
	 * 
	 * @param key
	 * @return {@link String}
	 */
	public String getMessage(String key) {
		return this.prop.getProperty(key);
	}

	/**
	 * get the message value
	 * 
	 * @param key
	 * @param obj[]
	 * @return {@link String}
	 */
	public String getMessage(String key, Object... obj) {
		String msg = this.getMessage(key);
		return MessageFormat.format(msg, obj);
	}

}
