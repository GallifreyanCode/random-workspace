##Configurations

###Derby
Go to %JAVA_HOME%/db/bin.
Execute the "startNetWorkServer" script and watch out for the derby instance to start.
Now open another CMD prompt also navigate to the db/bin folder and execute the "ij" script.
This should come up with a prompt " ij>. Now enter the following connect string:

    connect 'jdbc:derby://localhost:1527/JAVAEEEXAMPLE;create=true';

Now add the changelog table, this is used by dbdeploy for versioning.
    CREATE TABLE changelog ( change_number DECIMAL(22,0) NOT NULL, complete_dt TIMESTAMP NOT NULL, applied_by VARCHAR(100) NOT NULL, description VARCHAR(500) NOT NULL );
    ALTER TABLE changelog ADD CONSTRAINT Pkchangelog PRIMARY KEY (change_number);