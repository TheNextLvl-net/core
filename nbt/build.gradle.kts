plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "net.thenextlvl.core"
version = "1.4.2"

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":files"))
    compileOnly(project(":annotations"))

    testImplementation(project(":files"))

    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly("org.projectlombok:lombok:1.18.34")

    annotationProcessor("org.projectlombok:lombok:1.18.34")
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