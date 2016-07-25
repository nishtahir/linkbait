package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.core.util.DependencyUtils
import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.model.Configuration
import groovy.io.FileType
import org.jetbrains.annotations.NotNull

import java.util.jar.JarInputStream
import java.util.jar.Manifest
/**
 * Class loader to loadPluginsFromPath language tools dynamically
 * at runtime, this helps decouple to the project into
 * more manageable components.
 *
 * Information is loaded from the manifest
 */
class PluginLoader {

    /**
     * Base of the application package. Used to ensure that
     * we don't try to download duplicate packages.
     */
    public static final ROOT_PACKAGE_NAME = 'com.nishtahir'

    /**
     * Application config
     */
    Configuration configuration

    PluginLoader(@NotNull Configuration configuration) {
        this.configuration = configuration
    }

    /**
     * List of handlers loaded from plugins.
     */
    List<Plugin> handlers = []

    PluginClassLoader loader = new PluginClassLoader(new URL[0], getClass().classLoader)

    /**
     * @param path path to plugin folder
     * @throws FileNotFoundException
     */
    public void loadPluginsFromJar(File path) throws FileNotFoundException {
        path.eachFile(FileType.FILES) {
            if (it.name.endsWith(".jar")) {
                try {
                    loadPlugin(it)
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Loaded class should be a singleton.
     * @param files
     * @throws Exception
     */
    public void loadPlugin(File file) throws Exception {

        InputStream input = new FileInputStream(file);
        JarInputStream jarStream = new JarInputStream(input);
        Manifest mf = jarStream.getManifest();

        def attributes = mf.getMainAttributes()
        String className = attributes.getValue('Plugin-Class')

        String dependencies = attributes.getValue('Dependencies')
        DependencyResolver resolver = new DependencyResolver(repository: new File("data/repo"))

        List<File> files = []
        DependencyUtils.parseDependencies(dependencies).each {
            //All of this should already be in the classpath
            if (!it.group.startsWith(ROOT_PACKAGE_NAME)) {
                files.addAll(resolver.resolveArtifactWithDependencies(it))
            }
        }

        loadJars(files)
        loadJar(file)
        Class<Plugin> clazz = (Class<Plugin>) Class.forName(className, true, loader)

        def plugin = clazz.newInstance()
        handlers.add(plugin)

    }

    /**
     * Load list of jars into classpath.
     * @param jars
     */
    void loadJars(List<File> jars) {
        jars.each { jar ->
            loadJar(jar)
        }
    }

    /**
     * Load a single jar into the classpath.
     * @param jar
     */
    void loadJar(File jar) {
        loader.addURL(jar.toURI().toURL())
    }

}
