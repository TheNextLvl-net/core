plugins {
    id("java")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

group = "net.thenextlvl.core"
version = "1.0.9-pre1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":annotations"))
    compileOnly(project(":utils"))
    compileOnly(project(":api"))

    compileOnly("net.kyori:adventure-text-minimessage:4.14.0")

    compileOnly("org.projectlombok:lombok:1.18.28")
    compileOnly("org.jetbrains:annotations:24.0.0")

    testImplementation("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    testImplementation(project(":utils"))
    testImplementation(project(":api"))

    annotationProcessor("org.projectlombok:lombok:1.18.28")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
    repositories.maven {
        val branch = if (version.toString().contains("-pre")) "snapshots" else "releases"
        url = uri("https://repo.thenextlvl.net/$branch")
        credentials {
            username = extra["RELEASES_USER"].toString()
            password = extra["RELEASES_PASSWORD"].toString()
        }
    }
}