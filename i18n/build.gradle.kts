plugins {
    id("java")
    id("java-library")
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
version = "2.1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":utils"))
    api(project(":files"))

    compileOnlyApi("net.kyori:adventure-text-minimessage:4.20.0")
    compileOnly("net.kyori:adventure-text-logger-slf4j:4.20.0")
    compileOnly("org.jspecify:jspecify:1.0.0")

    testImplementation("net.kyori:adventure-text-minimessage:4.20.0")
    testImplementation("net.kyori:adventure-text-serializer-plain:4.20.0")
    testImplementation(project(":utils"))
    testImplementation(project(":files"))

    testImplementation(platform("org.junit:junit-bom:5.13.0-M2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation("net.kyori:adventure-text-logger-slf4j:4.20.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showCauses = true
        showExceptions = true
    }
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