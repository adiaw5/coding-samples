package net.mbreslow.codesamples.dependencyreporter;

import net.mbreslow.codesamples.javadependencyreporter.DependencyReportingClassloader;
import org.apache.catalina.loader.WebappLoader;

/**
 * WebappLoader extension that overloads getClassLoader() with a method that wraps the classloader in a
 * DependencyReportingClassloader.
 *
 * Activate this class by adding the following line in the Tomcat context.xml:
 * <Loader className="net.mbreslow.codesamples.dependencyreporter.TomcatDependencyReportingLoader" />
 * @author marc2112
 */
public class TomcatDependencyReportingLoader extends WebappLoader {
    public TomcatDependencyReportingLoader() {
        super();
    }

    public TomcatDependencyReportingLoader(ClassLoader parent) {
        super(parent);
    }

    public ClassLoader getClassLoader() {
        ClassLoader parent = super.getClassLoader();
        DependencyReportingClassloader delegator = new DependencyReportingClassloader(parent);
        return delegator;
    }
}
