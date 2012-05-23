##Liquibase Example

###Derby Configuration
Navigate to the derby directory.

    %JAVA_HOME%/db/bin/

Execute the following command to start Derby:

    startNetWorkServer


In the same directory you have to execute the ij script.
Once started, enter the following command to create the database:

    connect 'jdbc:derby://localhost:1527/liquibase;create=true';