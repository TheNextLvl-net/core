plugins {
    id("java")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

group = "net.thenextlvl.core"
version = "1.2.1"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.28")
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly(project(":annotations"))

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