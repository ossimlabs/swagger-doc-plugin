#SwaggerDocPlugin



##Purpose: To apply this plugin into your project in order to get the swagger specifications and save them to a json file. 

##How to use: Apply the following inside the build.gradle of the project you want to apply the plugin to
###-Add to dependencies:  
classpath "io.ossim.swagger:SwaggerDocPlugin:0.5"
###-Add to plugins:  
id "io.ossim.swagger.doc" version "0.5"
###-Add to subprojects:  
apply plugin: "io.ossim.swagger.doc"
                  swaggerDoc {
                      prefix = "\<yourPackage\>"
                  }
                     
###Run the plugin from your project terminal using: 
$ ./gradlew :\<moduleBeingScanned\>:generateSwaggerDocs

File should appear in your build directory


            