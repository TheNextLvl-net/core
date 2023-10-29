plugins {
    id("java")
    id("maven-publish")
}

group = "net.thenextlvl.core"
version = "1.3.6"

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":annotations"))

    testImplementation(project(":api"))
    testImplementation("com.google.code.gson:gson:2.10.1")

    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly("org.projectlombok:lombok:1.18.28")

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