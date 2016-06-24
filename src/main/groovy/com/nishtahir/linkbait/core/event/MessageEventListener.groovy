package com.nishtahir.linkbait.core.event

import com.google.common.eventbus.Subscribe

/**
 *
 */
interface MessageEventListener {

    /**
     *
     * @param event
     */
    @Subscribe
    public void handleMessageEvent(MessageEvent event);
}
