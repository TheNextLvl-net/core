plugins {
    id("java")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

group = "net.thenextlvl.core"
version = "1.0.8"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.28")
    compileOnly("org.jetbrains:annotations:24.0.0")
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
            if (extra.has("RELEASES_USER"))
                username = extra["RELEASES_USER"].toString()
            if (extra.has("RELEASES_PASSWORD"))
                password = extra["RELEASES_PASSWORD"].toString()
        }
    }
}