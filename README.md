# Alfresco Database Constraint

Module to help create a new LIST constraint extracting the allowed values from database.

This module do not provide a database drivers. If you want to use another database than PostgreSQL do not forget to put the driver on the Alfresco extension directory  

# Install

The component has been developed to install on top of an existing Alfresco 4.0, 4.1, 4.2 or 5.0 installation. The alf-db-constraints.amp needs to be installed into the Alfresco Repository using the Alfresco Module Management Tool:

      java -jar alfresco-mmt.jar install alf-db-constraints.amp /path/to/alfresco.war

You can also use the Alfresco Maven SDK to install or overlay the AMP during the build of a Repository project. See https://artifacts.alfresco.com/nexus/content/repositories/alfresco-docs/alfresco-lifecycle-aggregator/latest/plugins/alfresco-maven-plugin/advanced-usage.html for details.

# Build 
To build the module and its AMP files, run the following command from the base project directory:

     mvn install

The command will generate the '.amp' file called 'alf-db-constraints.amp'

# Using DBLIST Constraint

The 'DBLIST' type constraint creation are near 'LIST' constraint type. The mainly difference are the type and the used parameters. 

As a main parameter to be used in this kind of constraints, the SQL query is the only one every required. Following you can see one 'DBLIST' constraint example that use as constraint the UUID extracted from alf_node alfresco database table.

      <constraint  name="custom:noDoAlfresco"  type="DBLIST" >
			<parameter  name="query" ><value>SELECT uuid FROM alf_node</value></parameter>
		</constraint>
           



## Parameters Definition
