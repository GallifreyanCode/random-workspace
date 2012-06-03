## Learning more and new Spring features.
@WorkInProgress

* Spring 3.1.1
* Spring MVC
* Spring Data JPA
* Spring Security 3.1
* Spring WS
* JavaConfig!

###Deploy with Jetty 8

* Setup

Jetty has mapping issues with "/". You can disable the WebAppServlet and enabling the code within the web.xml. Another way is to overwrite the default jetty config by putting the following in your plugin configuration (this solution is configured by default).

			<configuration>
			  <scanIntervalSeconds>0</scanIntervalSeconds>
			  <webAppConfig>
			    <defaultsDescriptor>src/main/resources/webdefault.xml</defaultsDescriptor>
			  </webAppConfig>
			</configuration>

Copy the webdefault file from the Jetty distribution, and comment out this part:

			<!--   <servlet-mapping> -->
			<!--     <servlet-name>default</servlet-name> -->
			<!--     <url-pattern>/</url-pattern> -->
			<!--   </servlet-mapping> -->

* Build the application

			mvn clean install jetty:run

* Navigate to

			http://localhost:9773

###Deploy with Tomcat 7

* Build the application

			mvn clean install t7:run
* Navigate to

		 http://localhost:8080/spring-mvc-example