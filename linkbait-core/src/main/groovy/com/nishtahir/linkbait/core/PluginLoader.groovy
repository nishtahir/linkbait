package com.nishtahir.linkbait.core

import com.nishtahir.linkbait.core.util.DependencyUtils
import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.model.Configuration
import groovy.io.FileType
import org.jetbrains.annotations.NotNull

import java.util.jar.JarInputStream
import java.util.jar.Manifest

/**
 * Class pluginClassLoader to loadPluginsFromPath language tools dynamically
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
     * List of plugins loaded from plugins.
     */
    List<Plugin> plugins = []

    /**
     * Classloader where all the magic happens.
     */
    PluginClassLoader pluginClassLoader = new PluginClassLoader(new URL[0], getClass().classLoader)

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
    public void loadPlugin(File plugin) throws Exception {
        Manifest mf = getManifest(plugin)

        def attributes = mf.getMainAttributes()
        String pluginClassName = attributes.getValue('Plugin-Class')
        String dependencies = attributes.getValue('Dependencies')

        DependencyResolver resolver = new DependencyResolver(configuration.pluginRepository)

        List<File> files = []
        DependencyUtils.parseDependencies(dependencies).each {
            //All of this should already be in the classpath
            //After all, they are compile time dependencies for the framework
            if (!it.group.startsWith(ROOT_PACKAGE_NAME) && it.group != "linkbait") {
                files.addAll(resolver.resolveArtifactWithDependencies(it))
            }
        }

        addJarsToClasspath(files)
        addJarToClasspath(plugin)
        Class<Plugin> clazz = (Class<Plugin>) Class.forName(pluginClassName, true, pluginClassLoader)

        def pluginClass = clazz.newInstance()
        plugins.add(pluginClass)

    }

    /**
     *
     * @param jar
     * @return
     */
    private Manifest getManifest(File jar) {
        InputStream input = new FileInputStream(jar);
        JarInputStream jarStream = new JarInputStream(input);
        Manifest manifest = jarStream.getManifest();
        return manifest
    }

    /**
     * Load list of jars into classpath.
     * @param jars
     */
    void addJarsToClasspath(List<File> jars) {
        jars.each { jar ->
            addJarToClasspath(jar)
        }
    }

    /**
     * Loads a single jar onto the classpath.
     * @param jar file to load.
     */
    void addJarToClasspath(File jar) {
        pluginClassLoader.addURL(jar.toURI().toURL())
    }

}
