package org.gradle

import io.swagger.annotations.Api
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner

class GreetingPlugin implements Plugin<Project> {
    String prefix
    private Reflections reflections = new Reflections("omar", new TypeAnnotationsScanner(), new SubTypesScanner())

    @Override
    void apply(Project project) {
        project.task("testSwaggerStuff") {
            doFirst {
                getSwaggerAPIClasses("omar").forEach { println(it) }
                // TODO: Add swagger doc generation
            }
            doLast {
                println("Hello world")
            }
        }
    }

    /**
     * @param prefix Package prefix, to be used with ClasspathHelper.forPackage(String, ClassLoader...) )}* @return
     */
    public Set<Class<?>> getSwaggerAPIClasses(String prefix) {
        return reflections.getTypesAnnotatedWith(Api.class);
    }
}

// TODO: Change names of plugin, package, etc
//  Add configurable prefix value under extension (see gradle plugin development "extension")