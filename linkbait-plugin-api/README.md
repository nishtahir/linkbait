# linkbait-plugin-api

This api is used to develop plugins for the Linkbait platform.

# Usage

This library is available in through JitPack. Simply include it in your build script like so.

```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.gitlab.nishtahir:linkbait-plugin-api:-SNAPSHOT'
}
```

# Plugin manifest

In order for the framework to properly load your plugin, its properties must be defined in the manifest.
This can be done through Gradle using the jar 

```
jar {
  baseName = 'MySuperPoweredPlugin'
  version = '9001'
  manifest {
    attributes 'Plugin-Class' : 'com.mycompany.MyPlugin',
      'Plugin-Id' : 'MySuperPoweredPlugin',
      'Plugin-Version' : '1.0',
      'Plugin-Provider' : 'Your name here <Optional email>'
  }
}
```

# Creating a plugin

Simply extend `Plugin` and override the appropriate lifecycle methods.

```
public class MyFancyPlugin extends Plugin {

  public void start(PluginContext context){
  
  }
  
  public void stop(PluginContext context){
  
  }
}
```

# Plugin lifecycle

The plugin lifecycle consists of 2 main methods. `start` and `stop`.

```
+----------+
|          |
|   Start  |
|          |
+-----+----+
      |
      |
+-----+----+
|          |
|   Stop   |
|          |
+----------+

```

They are invoked when after the plugin is load; when the plugin is started and stoped
respectively. 

Register any handlers you wish to receive events in start along with
any other resources and do any cleanup needed in stop

# Creating handlers

Event handlers are used to notify your plugin when certain things happen. For example
when messages are recieved, reactions are added etc...

Simply implement an event handler and you are in business.

For example,

```
public class MessageHandler implements MessageEventListener {

  public void handleMessageEvent(MessageEvent event) {
    String messageContent = event.getMessage();
    //handle  message here
  }
}
```

