plugins {
    id("java")
    id("maven-publish")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
    withJavadocJar()
}

tasks.compileJava {
    options.release.set(21)
}

group = "net.thenextlvl.core"
version = "2.0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.36")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT") {
        exclude("org.jetbrains", "annotations")
    }

    annotationProcessor("org.projectlombok:lombok:1.18.36")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
    repositories.maven {
        val channel = if ((version as String).contains("-pre")) "snapshots" else "releases"
        url = uri("https://repo.thenextlvl.net/$channel")
        credentials {
            username = System.getenv("REPOSITORY_USER")
            password = System.getenv("REPOSITORY_TOKEN")
        }
    }
}