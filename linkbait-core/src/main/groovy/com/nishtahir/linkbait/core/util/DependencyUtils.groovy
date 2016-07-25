package com.nishtahir.linkbait.core.util

import com.nishtahir.linkbait.core.model.Dependency

/**
 * Created by nish on 7/21/16.
 */
class DependencyUtils {

    static Dependency parseDependency(String dependency){
        def list = dependency.split(':')
        return new Dependency(group: list[0], artifact: list[1], version: list[2])
    }

    static List<Dependency> parseDependencies(String dependencies){
        return dependencies.split(',').collect(){
            parseDependency(it)
        }
    }

}
