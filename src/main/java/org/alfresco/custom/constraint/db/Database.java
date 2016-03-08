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

import java.util.List;

/**
 * Database Connection Interface
 * 
 * @author Denys G Santos (gsdenys@gmail.com)
 * @since 1.0
 * @version 1.0
 */
public interface Database {

	/**
	 * Get the database connection
	 */
	public void getConnetion();
	
    /**
     * Set the connection URL
     * 
     * @param url - the connection URL
     */
	public void setConnectionUrl(String url);
			
	/**
	 * Set the connection driver 
	 * 
	 * @param driver - the driver URL
	 */
	public void setDatabaseDriver(String driver);
	
	/**
	 * Set the connection user
	 * 
	 * @param user - the user connection
	 */
	public void setUser(String user);

	/**
	 * Set the connection password
	 * 
	 * @param pwd - the connection password
	 */
	public void setPwd(String pwd);
	
	/**
	 * Execute SQL Query
	 * 
	 * @param query - Query to be executed
	 * @return {@link List} of {@link String}
	 */
	public List<String> executeQuery(String query);
}
 