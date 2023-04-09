plugins {
    id("java")
    id("maven-publish")
}

group = "net.thenextlvl.core"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly(project(":annotations"))

    annotationProcessor(project(":annotations"))
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
        repositories {
            maven {
                url = uri("https://repo.thenextlvl.net/releases")
                credentials {
                    username = extra["RELEASES_USER"].toString()
                    password = extra["RELEASES_PASSWORD"].toString()
                }
            }
        }
    }
}