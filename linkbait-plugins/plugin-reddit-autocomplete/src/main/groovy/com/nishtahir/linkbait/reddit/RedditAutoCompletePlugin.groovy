package com.nishtahir.linkbait.reddit

import com.nishtahir.linkbait.plugin.Plugin
import com.nishtahir.linkbait.plugin.PluginContext
import org.jetbrains.annotations.NotNull;

class RedditAutoCompletePlugin extends Plugin {

    RedditAutoCompleteListener listener

    @Override
    void start(@NotNull PluginContext context) {
        if (listener == null) {
            listener = new RedditAutoCompleteListener(context)
        }
        context.registerListener(listener)
    }

    @Override
    void stop(@NotNull PluginContext context) {
        if (listener != null) {
            context.unregisterListener(listener)
        }
    }

    @Override
    void onPluginStateChanged() {

    }
}
