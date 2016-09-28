#!/bin/sh

## Hack our GEM_HOME to make sure that the `rubygems` support can find our
# jars and unpacked gems in the given GEMFOLDER
export GEM_HOME="/Users/nish/Developer/gitlab/linkbait/build/linkbait-plugins/plugin-github-bot/gems"
export GEM_PATH="/Users/nish/Developer/gitlab/linkbait/build/linkbait-plugins/plugin-github-bot/gems"
export JARS_HOME=$GEM_HOME/jars
export JARS_LOCK=$GEM_HOME/Jars.lock

exec java -cp /Users/nish/.gradle/caches/modules-2/files-2.1/org.jruby/jruby-complete/9.1.4.0/c2d5f63e9e9ccdab09b40e4dbdee83d74ae0cce5/jruby-complete-9.1.4.0.jar org.jruby.Main -rjars/setup -S $@

