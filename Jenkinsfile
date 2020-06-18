String buildNodeDefault = "omar-build"

//noinspection GroovyAssignabilityCheck
properties([
        parameters([
                string(name: 'BUILD_NODE', defaultValue: buildNodeDefault, description: 'The build node to run on'),
                booleanParam(name: 'CLEAN_WORKSPACE', defaultValue: true, description: 'Clean the workspace at the end of the run')
        ]),
        pipelineTriggers([
                [$class: "GitHubPushTrigger"]
        ]),
        [$class: 'GithubProjectProperty', displayName: '', projectUrlStr: 'https://github.com/ossimlabs/omar-volume-cleanup'],
        buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '3', daysToKeepStr: '', numToKeepStr: '20')),
        disableConcurrentBuilds()
])

// We use the get[] syntax here because the first time a new branch of pipeline is loaded, the property does not exist.
node(params["BUILD_NODE"] ?: buildNodeDefault) {
    stage("Checkout Source") {
        // We want to start our pipeline in a fresh workspace since cleaning up afterwards is optional.
        // Needed because rerunning the tests on a dirty workspace fails.
        step([$class: 'WsCleanup'])
        checkout(scm)
    }

    stage("Load Variables") { // This is needed for Docker, Maven, and Sonarqube variables
        withCredentials([string(credentialsId: 'o2-artifact-project', variable: 'o2ArtifactProject')]) {
            step ([$class: "CopyArtifact",
                   projectName: o2ArtifactProject,
                   filter: "common-variables.groovy",
                   flatten: true])
        }

        load "common-variables.groovy"
    }

    stage("Build") {
        sh "gradle build -PdownloadMavenUrl=$MAVEN_DOWNLOAD_URL"
        archiveArtifacts "build/libs/*.jar"
        junit "build/test-results/**/*.xml"
    }

    stage("Publish Jar") {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
                          credentialsId: 'mavenCredentials',
                          usernameVariable: 'ORG_GRADLE_PROJECT_uploadMavenRepoUsername',
                          passwordVariable: 'ORG_GRADLE_PROJECT_uploadMavenRepoPassword']]) {
            sh """
                gradle publish -PuploadMavenRepoUrl=$MAVEN_UPLOAD_URL -PdownloadMavenUrl=$MAVEN_DOWNLOAD_URL
            """
        }
    }

    stage("Clean Workspace") {
        if (params["CLEAN_WORKSPACE"] == "true") step([$class: 'WsCleanup'])
    }
}