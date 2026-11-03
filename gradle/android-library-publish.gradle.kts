/**
 * Shared Maven / JitPack publishing for Android library modules.
 *
 * Prerequisites in the module's android {} block:
 *   publishing {
 *       singleVariant("release") {
 *           withSourcesJar()
 *       }
 *   }
 *
 * Coordinates: com.github.e-hai.AnalyticsKit-Android:<module>:<tag>
 */
apply(plugin = "maven-publish")

group = providers.gradleProperty("GROUP_ID").orElse("com.github.e-hai.AnalyticsKit-Android").get()
version = providers.gradleProperty("VERSION_NAME").orElse("0.1.0").get()

afterEvaluate {
    extensions.configure<PublishingExtension>("publishing") {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()

                pom {
                    name.set(project.name)
                    description.set("AnalyticsKit Android library module: ${project.name}")
                    url.set("https://github.com/e-hai/AnalyticsKit-Android")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("e-hai")
                            name.set("e-hai")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/e-hai/AnalyticsKit-Android.git")
                        developerConnection.set("scm:git:ssh://github.com:e-hai/AnalyticsKit-Android.git")
                        url.set("https://github.com/e-hai/AnalyticsKit-Android")
                    }
                }
            }
        }
    }
}
