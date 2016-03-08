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

import org.alfresco.custom.constraint.Msg;
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

	private Msg msg;
	private Connection conn = null;
	
	public DatabaseImpl() {
		msg = Msg.getInstance();
	}

	/**
	 * @see org.alfresco.custom.constraint.db.Database#executeQuery(String)
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

			while (rs.next()) {
				String str = rs.getString(1);
				allowedValues.add(str);
			}

			Object[] obj = new Object[] {query, allowedValues.size()};
			logger.debug(msg.getMessage(Msg.SUCCESS_EXECUTE_QUERY, obj));
		} catch (SQLException e) {
			logger.error(msg.getMessage(Msg.ERROR_EXECUTE_QUERY, query));
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				logger.error(msg.getMessage(Msg.ERROR_CLOSE_CONNECTION));
				e.printStackTrace();
			}
		}

		return allowedValues;
	}

	/**
	 * @see org.alfresco.custom.constraint.db.Database#getConnetion()
	 */
	@Override
	public void getConnetion() {
		try {
			Class.forName(this.driver);
			this.conn = DriverManager.getConnection(this.url, this.user, this.pwd);
		} catch (ClassNotFoundException e) {
			logger.error(msg.getMessage(Msg.ERROR_LOAD_DRIVER, driver));
			e.printStackTrace();
		} catch (SQLException e) {
			Object[] obj = new Object[] { this.url, this.user, this.pwd };
			logger.error(msg.getMessage(Msg.ERROR_OPEN_CONNECTION, obj));
			e.printStackTrace();
		}

	}

	/**
	 * @see org.alfresco.custom.constraint.db.Database#setConnectionUrl(java.lang.String)
	 */
	@Override
	public void setConnectionUrl(String url) {
		this.url = url;
	}

	/**
	 * @see org.alfresco.custom.constraint.db.Database#setDatabaseDriver(java.lang.String)
	 */
	@Override
	public void setDatabaseDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @see org.alfresco.custom.constraint.db.Database#setUser(java.lang.String)
	 */
	@Override
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @see org.alfresco.custom.constraint.db.Database#setPwd(java.lang.String)
	 */
	@Override
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
