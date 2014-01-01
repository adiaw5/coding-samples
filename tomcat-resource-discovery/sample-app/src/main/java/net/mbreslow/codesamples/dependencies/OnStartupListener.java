package net.mbreslow.codesamples.dependencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Loads resources when the application is initialized.
 *
 * First reads a context-parameter from web.xml called app-properties which is expected to point to a Properties file.
 * This is the first resource it loads and one that we expect to see reported in dependencies.txt.
 *
 * Within the appProperties file, we look for all properties that begin with inputfile. and load the values of them.
 *
 * We also look for properties prefixed with jdbc.connection and use these to make a JDBC connection directly via
 * DriverManager.  The properties required are:<ul>
 *     <li><jdbc.connection.driver - class name of the driver to use</li>
 *     <li>jdbc.connection.url - JDBC url of the connection</li>
 *     <li>jdbc.connection.username - username to connect to the database with</li>
 *     <li>jdbc.connection.password - password to connect to the database with</li>
 * </ul>
 *
 * Finally, we look for a property called datasource.name and expect it to be the JNDI name of a datasource resource.
 * If found, we lookup the DataSource in JNDI and make a connection to it.
 *
 * The expectation is that both methods of connecting to a JDBC resource will be captured by our SnoopingDriver.
 *
 * @author marc2112
 */
@WebListener()
public class OnStartupListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(OnStartupListener.class);
    public static final String READ_FILE_PROP_PREFIX = "inputfile.";
    public static final String JDBC_CONN_PROP_PREFIX = "jdbc.connection";
    public static final String DATASOURCE_PROP_PREFIX = "datasource";

    // Public constructor is required by servlet spec
    public OnStartupListener() {
    }

    // -------------------------------------------------------
    // OnStartupListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        final String propertiesFilePath = sce.getServletContext().getInitParameter("app-properties");
        if (propertiesFilePath != null) {
            InputStream is = sce.getServletContext().getClassLoader().getResourceAsStream(propertiesFilePath);
            if (is == null) {
                log.error("Unable to read properties file {}", propertiesFilePath);
            }
            else {
                try {
                    Properties appProperties = new Properties();
                    appProperties.load(is);
                    initApp(sce.getServletContext().getClassLoader(), appProperties);
                } catch (IOException e) {
                    log.error("Error reading properties file " + propertiesFilePath, e);
                }
            }

        }
        else {
            log.error("Expected context init-parameter {}", propertiesFilePath);
        }

    }

    /**
     * Initializes the application based on the info in the properties file
     * @param classLoader
     * @param appProperties
     */
    private void initApp(ClassLoader classLoader, Properties appProperties) {
        Enumeration propertyNameEnumeration = appProperties.propertyNames();
        while (propertyNameEnumeration.hasMoreElements()) {
            final String propName = (String) propertyNameEnumeration.nextElement();
            if (propName.startsWith(READ_FILE_PROP_PREFIX)) {
                final String propValue = appProperties.getProperty(propName);
                tryLoadFile(classLoader, propValue);
            }
        }
        tryConnectJdbc(classLoader, appProperties);
        tryConnectDatasource(classLoader, appProperties);
    }

    /**
     * Attempt to connect to JDBC connection
     * @param classLoader
     * @param appProperties
     */
    private void tryConnectJdbc(ClassLoader classLoader, Properties appProperties) {
        final String driverClass = appProperties.getProperty(JDBC_CONN_PROP_PREFIX+ ".driver");
        final String url = appProperties.getProperty(JDBC_CONN_PROP_PREFIX+ ".url");
        final String user = appProperties.getProperty(JDBC_CONN_PROP_PREFIX+ ".username");
        final String password = appProperties.getProperty(JDBC_CONN_PROP_PREFIX+ ".password");

        if (driverClass != null && url != null && user != null && password != null) {
            loadDriverClass(classLoader, driverClass);
            try {
                final Connection connection = DriverManager.getConnection(url, user, password);
                if (connection != null && connection.isClosed() == false) {
                    log.info("Established JDBC connection to {}", url);
                }
            } catch (Exception e) {
                log.error("Unable to make jdbc connection to url " + url + " with username " + user, e);
            }
        }
        else {
            log.info("Not attempting to make JDBC connection because JDBC properties are not all provided");
        }
    }

    private void tryConnectDatasource(ClassLoader classLoader, Properties appProperties) {
        final String datasourceName = appProperties.getProperty(DATASOURCE_PROP_PREFIX+ ".name");
        if (datasourceName != null) {
            log.info("Trying to connect to datasource " + datasourceName);
        }
        try {
            Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            DataSource ds = (DataSource) envCtx.lookup(datasourceName);
            Connection c = ds.getConnection();
        } catch (NamingException e) {
            log.error("Unable to lookup datasource named " + datasourceName, e);
        } catch (SQLException e) {
            log.error("Unable to get connection from datasource named " + datasourceName, e);
        }
    }

    private void loadDriverClass(ClassLoader classLoader, String driverClass) {
        try {
            Class.forName(driverClass);
        }
        catch (ClassNotFoundException e) {
            log.error("Unable to load JDBC driver with class {}", driverClass);
        }
    }

    /**
     * Tries to load the file using the classloader
     * @param classLoader
     * @param propValue
     */
    private void tryLoadFile(ClassLoader classLoader, String propValue) {
        InputStream is = classLoader.getResourceAsStream(propValue);
        if (is != null) {
            try {
                // consume file but do nothing with it
                BufferedInputStream bis = new BufferedInputStream(is);
                int bytesRead = 0;
                while (bis.read() > 0) {
                    bytesRead++;
                }
                log.info("Read {} bytes from file {}", bytesRead, propValue);
            } catch (IOException e) {
                log.error("Error reading resource " + propValue, e);
            }
        }
        else {
            log.error("Unable to find resource named {}", propValue);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

}
