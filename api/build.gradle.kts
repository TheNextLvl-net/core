plugins {
    id("java")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

group = "net.thenextlvl.core"
version = "4.0.6"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.28")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly(project(":annotations"))

    testImplementation("com.google.code.gson:gson:2.10.1")

    implementation(project(":utils"))

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