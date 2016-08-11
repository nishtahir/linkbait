# Linkbait core

Core library for linkbait.

# Features

* Sevices are used to run connections to messaging platforms
* Plugins are loaded dynamically at runtime
* Eventbus is for message passing between the core and plugins
* Plugin compile time dependencies not bundles with plugins are downloaded 
and added to the classpath at runtime. (No more fatjars)