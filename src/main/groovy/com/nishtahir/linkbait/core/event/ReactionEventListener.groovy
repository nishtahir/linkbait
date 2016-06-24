package com.nishtahir.linkbait.core.event

import com.google.common.eventbus.Subscribe

/**
 *
 */
interface ReactionEventListener {

    /**
     *
     * @param event
     */
    @Subscribe
    public void handleMessageEvent(MessageEvent event);
}
