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
    compileOnly("org.jetbrains:annotations:25.0.0")

    api(project(":utils"))
    compileOnly(project(":annotations"))

    compileOnlyApi("com.google.code.gson:gson:2.11.0")
    testImplementation("com.google.code.gson:gson:2.11.0")

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