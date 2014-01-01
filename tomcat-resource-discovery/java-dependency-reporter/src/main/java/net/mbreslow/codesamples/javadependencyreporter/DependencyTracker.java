package net.mbreslow.codesamples.javadependencyreporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Runtime;import java.lang.String;import java.lang.System;import java.lang.Thread;
import java.util.*;

/**
 * Singleton that keeps track of dependency strings and jdbc connection information throughout the lifecycle of an
 * application.  Via a shutdown hook, the list of dependencies are output to a text file when the application exits
 * gracefully.
 *
 * The output file that is produced on shutdown can be configured via the system property 'dependencies.txt'.  By default
 * it is /tmp/dependencies.txt
 *
 * @author marc2112
 */
public class DependencyTracker {
    private final SortedSet<String> dependencies = new TreeSet<String>();
    private final SortedMap<String, Properties> jdbcConnectionStrings = new TreeMap<String,Properties>();
    private final String outputFile = System.getProperty("dependencies.txt", "/tmp/dependencies.txt");

    /**
     * singleton instance
     */
    private static final DependencyTracker instance;

    // -- initialize the singleton
    static {
        System.out.println("DependencyReportingClassloader Activated.  On shutdown, look for dependencies.txt");
        instance = new DependencyTracker();
        instance.addShutdownHook();

    }

    /**
     * Private constructor (singleton)
     */
    private DependencyTracker() {
        super();
    }

    /**
     * Get a handle to the Singleton
     * @return DependencyTracker singleton instance
     */
    public static DependencyTracker getInstance() {
        return instance;
    }

    /**
     * Record a resource dependency
     * @param resource path to the resource
     */
    public void addDependency(String resource) {
        dependencies.add(resource);
    }

    /**
     * Record a JDBC connection string and (optionally) any associated connection properties
     * @param url jdbc url
     * @param properties jdbc properties
     */
    public void addJdbcConnectionString(String url, Properties properties) {
        if (properties == null) {
            properties = new Properties();
        }
        jdbcConnectionStrings.put(url, properties);
    }


    /**
     * Produce a textfile listing the dependencies at shutdown
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @java.lang.Override
            public void run() {
                // clone to avoid concurrent modification exeptions
                final String[] dependenciesArray = dependencies.toArray(new String[dependencies.size()]);
                System.out.println("Producing dependencies file [" + outputFile+ "]");
                File dependenciesFile = new File(outputFile);
                try {
                    FileWriter writer = new FileWriter(dependenciesFile);
                    final String SEP = System.getProperty("line.separator");

                    writer.append("------------------------------------").append(SEP);
                    writer.append("------------ RESOURCES -----------").append(SEP);
                    writer.append("------------------------------------").append(SEP);
                    for (String dependency : dependenciesArray) {
                        if (!dependency.endsWith(".class")) {
                            writer.append(dependency).append(SEP);
                        }
                    }

                    if (jdbcConnectionStrings.size() > 0) {
                        writer.append(SEP);
                        writer.append("------------------------------------").append(SEP);
                        writer.append("-------- JDBC CONNECTIONS ----------").append(SEP);
                        writer.append("------------------------------------").append(SEP);
                        for (Map.Entry<String,Properties> entry : jdbcConnectionStrings.entrySet()) {
                            final String url = entry.getKey();
                            writer.append(url).append(':').append(SEP);
                            Properties p = entry.getValue();
                            if (p.size() > 0) {
                                for (Object key : p.keySet()) {
                                    writer.append("--> ").append(key.toString()).append(':');
                                    writer.append(p.get(key) == null ? "null" : p.get(key).toString()).append(SEP);
                                }
                            }
                            writer.append(SEP);
                        }
                    }


                    writer.append(SEP);
                    writer.append("------------------------------------").append(SEP);
                    writer.append("----------- CLASSES ----------------").append(SEP);
                    writer.append("------------------------------------").append(SEP);
                    for (String dependency : dependenciesArray) {
                        if (dependency.endsWith(".class")) {
                            writer.append(dependency).append(SEP);
                        }
                    }

                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Error saving dependencies to file " + dependenciesFile.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        });
    }

}
