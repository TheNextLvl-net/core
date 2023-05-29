plugins {
    id("java")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

group = "net.thenextlvl.core"
version = "1.1.3"

repositories {
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly(project(":annotations"))

    implementation(project(":api"))

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