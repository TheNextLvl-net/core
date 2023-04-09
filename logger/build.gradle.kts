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
    compileOnly("org.slf4j:slf4j-api:2.0.6")
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly(project(":annotations"))

    implementation(project(":api"))
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