package io.ossim.swagger

import com.fasterxml.jackson.core.JsonProcessingException
import io.swagger.annotations.Api
import io.swagger.models.Swagger
import io.swagger.servlet.Reader
import io.swagger.util.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.reflections.Reflections

import java.util.jar.JarEntry
import java.util.jar.JarFile

class SwaggerDocPlugin implements Plugin<Project> {
    String prefix

    @Override
    void apply(Project project) {
        project.apply plugin: 'java'
        SwaggerDocExtension extension = project.getExtensions()
                .create("swaggerDoc", SwaggerDocExtension.class)
        prefix = extension.getPrefix()

        project.task(type: Jar, "fatJar") {
            dependsOn("assemble")
            archiveBaseName = 'fatjar'
            from { project.configurations.compile.collect { it.isDirectory() ? it : project.zipTree(it) } }
            with project.jar
        }
        project.task("generateSwaggerDocs") {
            dependsOn("fatJar")
            doFirst {
                try { throw new RuntimeException("DEBUG") } catch (e) {/* do nothing */}
                String fatJarPath = (project.tasks.getByName("fatJar") as Jar).archiveFile.get().asFile.path
                // TODO: Remove hard coded prefix and use task configuration instead.
                Swagger swagger = new Swagger()
                Set<Class> classes = getSwaggerAPIClasses("omar", fatJarPath)
                println prefix
                println "Swagger Spec Classes:"
                for(Class c: classes) {
                    println c
                }
                Reader.read(swagger, classes)
                String swaggerJson = null
                if (swagger) {
                    try {
                        swaggerJson = Json.mapper().writeValueAsString(swagger)
                    } catch (JsonProcessingException e) {
                        e.printStackTrace()
                    }
                }
                println swaggerJson

                new File(project.buildDir,'swaggerJson.Json').withWriter('utf-8') { writer ->
                    writer.writeLine swaggerJson
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
        URLClassLoader cl = URLClassLoader.newInstance(urls, this.class.getClassLoader())
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement()
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue
            }
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6)
            className = className.replace('/', '.')
            //println("Found class: $className")
            try {
                Class c = cl.loadClass(className)
            } catch (Throwable exception) {
                //println("Skipping: $className - ${exception.class.name}: $exception.message")
            }
        }
        Reflections reflections = new Reflections(cl)
        return reflections.getTypesAnnotatedWith(Api.class)
    }
}
// TODO: Add configurable prefix value under extension (see gradle plugin development "extension")