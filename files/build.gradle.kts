plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

group = "net.thenextlvl.core"
version = "1.0.5"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    compileOnly("org.jetbrains:annotations:24.1.0")

    api(project(":utils"))
    compileOnly(project(":annotations"))

    compileOnlyApi("com.google.code.gson:gson:2.10.1")
    testImplementation("com.google.code.gson:gson:2.10.1")

    annotationProcessor("org.projectlombok:lombok:1.18.34")
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