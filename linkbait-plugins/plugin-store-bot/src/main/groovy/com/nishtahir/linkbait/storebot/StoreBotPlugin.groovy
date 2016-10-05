package com.nishtahir.linkbait.storebot

import com.nishtahir.linkbait.plugin.LinkbaitPlugin
import com.nishtahir.linkbait.plugin.PluginContext
import org.jetbrains.annotations.NotNull;

class StoreBotPlugin extends LinkbaitPlugin {

    StoreBotListener listener

    @Override
    void start(@NotNull PluginContext context) {
        if (listener == null) {
            listener = new StoreBotListener(context)
        }

        context.registerListener(listener)
    }

    @Override
    void stop(@NotNull PluginContext context) {
        context.unregisterListener(listener)
    }

    @Override
    void onPluginStateChanged() {

    }
}
