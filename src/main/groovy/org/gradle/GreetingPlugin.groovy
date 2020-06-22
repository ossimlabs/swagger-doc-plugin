package org.gradle

import io.swagger.annotations.Api
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.reflections.Reflections
import io.swagger.models.Swagger
import io.swagger.servlet.Reader
import swagger.SwaggerService
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.concurrent.*

class GreetingPlugin implements Plugin<Project> {
    String prefix
    @Override
    void apply(Project project) {
        project.apply plugin: 'java'
        project.task(type: Jar, "fatJar") {
            dependsOn("assemble")
            archiveBaseName = 'fatjar'
            from { project.configurations.compile.collect { it.isDirectory() ? it : project.zipTree(it) } }
            with project.jar
        }
        project.task("generateSwaggerDocs") {
            dependsOn("fatJar")
            doFirst {
                String fatJarPath = (project.tasks.getByName("fatJar") as Jar).archiveFile.get().asFile.path
                // TODO: Remove hard coded prefix and use task configuration instead.
                getSwaggerAPIClasses("omar", fatJarPath).forEach {
                    println(it)
                    Swagger swagger = new Swagger()

                    Reader.read(swagger, it)
                    String res = SwaggerService.getJsonDocument(swagger)
                    println res
                }
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
    Set<Class> getSwaggerAPIClasses(String prefix, String jarPath) {
        JarFile jarFile = new JarFile(jarPath)
        Enumeration<JarEntry> e = jarFile.entries()
        URL[] urls = [new URL("jar:file:$jarPath!/")]
        URLClassLoader cl = URLClassLoader.newInstance(urls)
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement()
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue
            }
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6)
            className = className.replace('/', '.')
            println("Found class: $className")
            try {
                Class c = cl.loadClass(className)
            } catch (Throwable exception) {
                println("Skipping: $className - ${exception.class.name}: $exception.message")
            }
        }
        Reflections reflections = new Reflections(cl)
        return reflections.getTypesAnnotatedWith(Api.class)
    }
}
// TODO: Change names of plugin, package, etc
//  Add configurable prefix value under extension (see gradle plugin development "extension")