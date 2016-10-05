package com.nishtahir.linkbait.github

import com.nishtahir.linkbait.plugin.{LinkbaitPlugin, PluginContext}

/**
  * Created by nish on 10/4/16.
  */
class GithubPlugin extends LinkbaitPlugin {

  var handler : GithubHandler = _

  override def start(context: PluginContext): Unit = {
    if(handler == null){
      handler = new GithubHandler(context)
    }
    context.registerListener(handler)
  }

  override def stop(context: PluginContext): Unit = {
    handler = null
    context.unregisterListener(handler)
  }

  override def onPluginStateChanged(): Unit = {

  }
}