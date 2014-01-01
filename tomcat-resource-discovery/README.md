# Tomcat Resource Discovery Project

## Overview

The intention of this code sample is to demonstrate how one could discover configuration files and JDBC data sources that
an application running in Tomcat depends on.  The goal goal is to produce a Jar file drop-in that will produce an inventory
of configuration files and JDBC data sources that were loaded by the application during its runtime lifecycle.

## Implementation

### Resource Discovery

In addition to the System Classloader, Tomcat creates a common classloader that exposes shared libraries across all the
applications as well as an isolated web-application classloader for each webapp that is deployed.  The webapp classloaders
by default load resources from the web archive first and, if not found, look for the resources in the parent (common and
system) classloaders.  Because the system classloader isn't consulted each time a resource is loaded, we need to hook
into both the webapp classloaders and the system classloaders to ensure that we see all resource requests.  For more
information on Tomcat's classloaders, see this [link](http://tomcat.apache.org/tomcat-7.0-doc/class-loader-howto.html "Tomcat Classloaders").

In this solution, we end up with two drop-in JAR files to be deployed.  One that captures resources loaded by the
System classloader and another that captures resources loaded by the webapp classloaders.  Two jar files are required
because the webapp classloader extends a class that is packaged in catalina.jar and which is loaded by Tomcat's bootstrap
service.

### JDBC Datasource Discovery

To satisfy the requirement of tracking JDBC dependencies, a Datasource Driver implementation is provided that keeps track
of all of the JDBC connection requests that are made from an application.  When an application requests a JDBC connection,
[DriverManager](http://docs.oracle.com/javase/7/docs/api/java/sql/DriverManager.html) is the factory class that use used
to produce the connections.

The DriverManager factory goes through its list of registered drivers in order passing the JDBC URL to each one and
requests a connection.  If the Driver returns `null` then DriverManager continues to ask the next registered Driver for
a connection and stops when it either receives a `Connection` or reaches the last driver in the list.

The SnoopingDriver that is delivered is registered first and always returns `null` when asked for a Connection.  Each
time it is asked for a Connection or if it accepts a URL, it records the URL string and the Connection properties so they
can be logged later.

## Modules

The code sample is comprised of 3 modules.

### java-dependency-reporter

This JAR contains most of the code we use in the solution.  It should be dropped into an ext subfolder under Tomcat (the
folder doesn't exist by default) and added to the system classpath via the bin/setenv.sh file.

#### DependencyTracker

Singleton that keeps track of dependency strings and jdbc connection information throughout the lifecycle of an
application.  Via a shutdown hook, the list of dependencies are output to a text file when the application exits
gracefully.

The output file that is produced on shutdown can be configured via the system property 'dependencies.txt'.  By default
it is /tmp/dependencies.txt

#### DependencyReportingClassloader
ClassLoader wrapper that records all resource requests that it sees with the
DependencyTracker singleton.  Activate this classloader by adding this system property to the JVM arguments
`-Djava.system.class.loader=net.mbreslow.codesamples.javadependencyreporter.DependencyReportingClassloader`

As this class is expected to be one of the first classes loaded by the JVM, it also registers the SnoopingDriver
so it will be the first Driver that DriverManager tracks.

#### SnoopingDriver
Silently tracks all JDBC connection requests and passes the information off to the DependencyTracker

### tomcat-dependency-reporter

This JAR contains one class that Tomcat uses when initializing web applications.  The `TomcatDependencyReportingLoader`
class is a WebappLoader extension that overloads getClassLoader() with a method that wraps the classloader in a
DependencyReportingClassloader.

Activate this class by adding the following line in the Tomcat context.xml:
```
<Loader className="net.mbreslow.codesamples.dependencyreporter.TomcatDependencyReportingLoader" />
```

### sample-app

This is a sample application used to test the process.  It has no functionality other then to load resources and make
JDBC connections as part of a ServletContextListener.

The `OnStartupListener` class loads resources when the application is initialized.

First reads a context-parameter from web.xml called app-properties which is expected to point to a Properties file.
This is the first resource it loads and one that we expect to see reported in dependencies.txt. 

Within the appProperties file, we look for all properties that begin with inputfile. and load the values of them.

We also look for properties prefixed with jdbc.connection and use these to make a JDBC connection directly via 
DriverManager.  The properties required are:

* jdbc.connection.driver - class name of the driver to use
* jdbc.connection.url - JDBC url of the connection
* jdbc.connection.username - username to connect to the database with
* jdbc.connection.password - password to connect to the database with

Finally, we look for a property called datasource.name and expect it to be the JNDI name of a datasource resource.
If found, we lookup the DataSource in JNDI and make a connection to it.

The expectation is that both methods of connecting to a JDBC resource will be captured by our SnoopingDriver.

## Installation Instructions

* Build all the artifacts by running `mvn package`
* Download and unpack Tomcat 7.  We'll refer to the directory that you unpacked Tomcat to as $CATALINA_HOME
* Under the $CATALINA_HOME directory, create a new directory called `ext`
* Copy java-dependency-reporter/target/java-dependency-reporter-1.0-SNAPSHOT.jar to $CATALINA_HOME/ext
* Copy tomcat-dependency-reporter/target/tomcat-dependency-reporter-1.0-SNAPSHOT.jar to $CATALINA_HOME/lib
* Copy sample-app/target/dependencies-app-1.0-SNAPSHOT.war to $CATALINA_HOME/webapps
* Copy the MySQL JDBC jar to $CATALINA_HOME/lib from http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.28.tar.gz
* Create a new file called setenv.sh under $CATALINA_HOME/bin.  The file contents should include

```

    CLASSPATH=$CATALINA_HOME/ext/java-dependency-reporter-1.0-SNAPSHOT.jar
    CATALINA_OPTS="-Djava.system.class.loader=net.mbreslow.codesamples.javadependencyreporter.DependencyReportingClassloader"
```

* Edit $CATALINA_HOME/conf/context.xml and add the following lines within the <Context> block

```
    
    <Loader className="net.mbreslow.codesamples.dependencyreporter.TomcatDependencyReportingLoader" />

    <Resource name="jdbc/TestDB" auth="Container" type="javax.sql.DataSource"
               maxActive="100" maxIdle="30" maxWait="10000"
               username="javauser" password="javadude" driverClassName="com.mysql.jdbc.Driver"
               url="jdbc:mysql://localhost:3306/javatest"/>
```

* Create a mysql database on localhost named javatest.  Create a user javauser identified by 'javadude' that has permission to connect.
* Start Tomcat (bin/startup.sh)
* Wait 10 seconds
* Stop Tomcat (bin/shutdown.sh)
* You should find a file /tmp/dependencies.txt that shows the dependencies of the application