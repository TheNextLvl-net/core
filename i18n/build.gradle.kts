plugins {
    id("java")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

group = "net.thenextlvl.core"
version = "1.0.15"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly(project(":annotations"))
    compileOnly(project(":utils"))
    compileOnly(project(":files"))

    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")

    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly("org.jetbrains:annotations:24.1.0")

    testImplementation("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    testImplementation(project(":utils"))
    testImplementation(project(":files"))

    annotationProcessor("org.projectlombok:lombok:1.18.32")
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