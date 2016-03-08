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
package org.alfresco.custom.constraint.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of {@link Database} interface
 * 
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @since 1.0
 * @version 1.0
 */
public class DatabaseImpl implements Database {
	private static Log logger = LogFactory.getLog(DatabaseImpl.class);

	private String url;
	private String driver;
	private String user;
	private String pwd;

	private Connection conn = null;
	
	/**
	 * @see org.alfresco.custom.database.Database#executeQuery(String)
	 */
	@Override
	public List<String> executeQuery(String query) {
		List<String> allowedValues = new ArrayList<String>();
		
		this.getConnetion();
		
		Statement stmt = null;
		ResultSet rs;

		try {
			stmt = this.conn.createStatement();
			rs = stmt.executeQuery(query);
			
			logger.debug("SQL executed: " + query );
			
			int count = 0;
			while (rs.next()) {
				String str = rs.getString(1);
				allowedValues.add(str);
				count++;
			}
			
			logger.debug("The query return " + count + " values");
		} catch (SQLException e) {
			logger.error("Error when try to execute query [" + query + "]");
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				logger.error("Error when try to close connection");
				e.printStackTrace();
			}
		}
		
		return allowedValues;
	}
	
	/**
	 * @see org.alfresco.custom.database.Database#getConnetion()
	 */
	@Override
	public void getConnetion() {
		try {
			Class.forName(this.driver);
			logger.debug("Driver Loaded successfully");

			this.conn = DriverManager.getConnection(this.url, this.user, this.pwd);
			logger.debug("Connection created successfully");

		} catch (ClassNotFoundException e) {
			logger.error("Error when try to load driver. Check if JDBC are in the classpath");
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error("Error when try to create connection. Check if the parameters [url="
					+ this.url
					+ " user="
					+ this.user
					+ " pwd="
					+ this.pwd
					+ "]");
			e.printStackTrace();
		}

	}

	/**
	 * @see org.alfresco.custom.database.Database#setConnectionUrl(java.lang.String)
	 */
	@Override
	public void setConnectionUrl(String url) {
		this.url = url;
	}

	/**
	 * @see org.alfresco.custom.database.Database#setDatabaseDriver(java.lang.String)
	 */
	@Override
	public void setDatabaseDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @see org.alfresco.custom.database.Database#setUser(java.lang.String)
	 */
	@Override
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @see org.alfresco.custom.database.Database#setPwd(java.lang.String)
	 */
	@Override
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
