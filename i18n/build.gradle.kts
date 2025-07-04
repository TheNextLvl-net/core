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
version = "3.2.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":utils"))
    api(project(":files"))

    compileOnly("org.jspecify:jspecify:1.0.0")
    compileOnlyApi("com.mojang:brigadier:1.0.500")
    compileOnlyApi("net.kyori:adventure-text-logger-slf4j:4.23.0")
    compileOnlyApi("net.kyori:adventure-text-minimessage:4.23.0")

    testImplementation("com.mojang:brigadier:1.0.500")
    testImplementation("net.kyori:adventure-text-logger-slf4j:4.23.0")
    testImplementation("net.kyori:adventure-text-minimessage:4.23.0")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.slf4j:slf4j-simple:2.1.0-alpha1")
    testImplementation(platform("org.junit:junit-bom:6.0.0-SNAPSHOT"))
    testImplementation(project(":files"))
    testImplementation(project(":utils"))
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