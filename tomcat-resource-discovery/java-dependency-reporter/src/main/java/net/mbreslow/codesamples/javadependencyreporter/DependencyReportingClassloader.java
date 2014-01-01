package net.mbreslow.codesamples.javadependencyreporter;

import java.io.InputStream;import java.lang.ClassLoader;import java.lang.Override;import java.lang.String;
import java.net.URL;

/**
 * ClassLoader wrapper that records all resource requests that it sees with the DependencyTracker singleton.  Activate
 * this classloader by adding this system property to the JVM arguments:
 *
 * -Djava.system.class.loader=net.mbreslow.codesamples.javadependencyreporter.DependencyReportingClassloader
 *
 * As this class is expected to be one of the first classes loaded by the JVM, it also registers the SnoopingDriver
 * so it will be the first Driver that DriverManager tracks.
 * @author marc2112
 */
public class DependencyReportingClassloader extends ClassLoader {
    static {
        SnoopingDriver.register();
    }

    public DependencyReportingClassloader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public InputStream getResourceAsStream(String s) {
        DependencyTracker.getInstance().addDependency(s);
        return super.getResourceAsStream(s);
    }

    @Override
    public URL getResource(String s) {
        DependencyTracker.getInstance().addDependency(s);
        return super.getResource(s);
    }
}
