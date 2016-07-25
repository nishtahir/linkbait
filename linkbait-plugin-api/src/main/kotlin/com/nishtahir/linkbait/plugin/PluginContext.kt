package com.nishtahir.linkbait.plugin

import com.nishtahir.linkbait.plugin.model.Configuration

/**
 * Created by nish on 7/19/16.
 */
interface PluginContext {

    fun getPluginState();

    fun getConfiguration() : Configuration;

    fun registerHander()
}