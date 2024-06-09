plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "net.thenextlvl.core"
version = "1.4.1"

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
    testImplementation("com.google.code.gson:gson:2.10.1")

    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly("org.projectlombok:lombok:1.18.32")

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