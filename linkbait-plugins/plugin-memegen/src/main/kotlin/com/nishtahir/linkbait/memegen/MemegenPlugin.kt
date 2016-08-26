package com.nishtahir.linkbait.memegen

import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.PluginContext
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.jar.JarFile


class MemegenPlugin : Plugin() {

    val TEMPLATES_FILE = "templates"

    init {
        //We have to do some hackery here because we don't have access
        //to folders as files.
        val jarFile = JarFile(javaClass.protectionDomain.codeSource.location.path)
        val jarEntries = jarFile.entries()
        while (jarEntries.hasMoreElements()) {
            val entry = jarEntries.nextElement()
            if (entry.name.startsWith(TEMPLATES_FILE) && entry.name.endsWith("png", true)) {
                val input = jarFile.getInputStream(entry);
                FileUtils.copyInputStreamToFile(input, File("data/${entry.name}"))
            }
        }
    }

    var memegenHandler: MemegenHandler? = null

    override fun start(context: PluginContext) {
        memegenHandler = MemegenHandler(context)
        memegenHandler?.let {
            context.registerListener(it)
        }
    }

    override fun stop(context: PluginContext) {
        memegenHandler?.let {
            context.unregisterListener(it)
        }
    }

    override fun onPluginStateChanged() {
    }

}


