##Java EE 6 Example
@Under Construction
###Requirements

 * Java 7 JDK

###Derby Configuration
Navigate to the derby directory.

    %JAVA_HOME%/db/bin/

Execute the following command to start Derby:

    startNetWorkServer


In the same directory you have to execute the ij script.
Once started, enter the following command to create the database:

    connect 'jdbc:derby://localhost:1527/JAVAEEEXAMPLE;create=true';

Now create the changelog table, this is used by dbdeploy for versioning.

    CREATE TABLE changelog (change_number DECIMAL(22,0) NOT NULL,
                            complete_dt TIMESTAMP NOT NULL,
                            applied_by VARCHAR(100) NOT NULL,
                            description VARCHAR(500) NOT NULL);
And finally set the primary key.

    ALTER TABLE changelog ADD CONSTRAINT Pkchangelog PRIMARY KEY (change_number);