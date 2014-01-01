package net.mbreslow.codesamples.javadependencyreporter;

import java.lang.Override;import java.lang.String;import java.lang.System;
import java.sql.*;
import java.util.Properties;

/**
 * Silently tracks all JDBC connection requests and passes the information off to the DependencyTracker
 * @author marc2112
 */
public class SnoopingDriver implements Driver {

    @Override
    public Connection connect(String s, Properties properties) throws SQLException {
        System.out.println("snoop connect: " + s);
        DependencyTracker.getInstance().addJdbcConnectionString(s, properties);
        return null;
    }

    @Override
    public boolean acceptsURL(String s) throws SQLException {
        System.out.println("snoop acceptsURL: " + s);
        DependencyTracker.getInstance().addJdbcConnectionString(s, null);
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 1;
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    public static void register() {
        try {
            DriverManager.registerDriver(new SnoopingDriver());
        } catch (SQLException e) {
            System.err.println("Unable to register SnoopingDriver");
            e.printStackTrace();
        }
    }
}
