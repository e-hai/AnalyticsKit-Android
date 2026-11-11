/**
 * Shared Maven / GitHub Packages publishing for Android library modules.
 *
 * Prerequisites in the module's android {} block:
 *   publishing {
 *       singleVariant("release") {
 *           withSourcesJar()
 *       }
 *   }
 *
 * Coordinates: com.github.e-hai.AnalyticsKit-Android:<module>:<version>
 */
apply(plugin = "maven-publish")

group = providers.gradleProperty("GROUP_ID").orElse("com.github.e-hai.AnalyticsKit-Android").get()
version = providers.gradleProperty("VERSION_NAME").orElse("1.0.0").get()

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

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/e-hai/AnalyticsKit-Android")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR") ?: "e-hai"
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
