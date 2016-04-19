package com.nishtahir.linkbait

import com.nishtahir.linkbait.model.Plugin
import com.nishtahir.linkbait.request.RequestHandler
import com.nishtahir.linkbait.util.JsonUtils
import groovy.transform.Canonical
import org.apache.commons.io.FileUtils

/**
 * Class loader to loadPluginsFromPath language tools dynamically
 * at runtime, this helps decouple to the project into
 * more manageable components.
 */
@Singleton
@Canonical
class PluginLoader {

    public static final PLUGIN_FILE = 'plugin.json'

    List<RequestHandler> handlers = [];

    /**
     * @param path path to plugin folder
     * @throws FileNotFoundException
     */
    public void loadPluginsFromPath(URI path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Path not found");
        }
        try {
            loadPlugin(file);
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    /**
     * Loaded class should be a singleton.
     * @param files
     * @throws Exception
     */
    public void loadPlugin(URL[] files) throws Exception {
        URLClassLoader child = new URLClassLoader(files, this.getClass().getClassLoader())
        String json = child.getResourceAsStream(PLUGIN_FILE).text
        Plugin plugin = JsonUtils.jsonToPlugin(json)

        Class<RequestHandler> clazz = (Class<RequestHandler>) Class.forName(plugin.handler, true, child)
        RequestHandler instance = clazz.newInstance()
        handlers.add(instance);
    }

    /**
     *
     * @param file
     * @throws Exception
     */
    public void loadPlugin(File file) throws Exception {
        if (file.isDirectory()) {
            FileUtils.listFiles(file, getExtensions(), false).each {
                loadPlugin(it)
            }
        } else {
            loadPlugin([file.toURI().toURL()] as URL[])
        }
    }

    /**
     * @return File extensions for plugins to loadPluginsFromPath
     */
    public static String[] getExtensions() {
        return ["jar"]
    }
}

