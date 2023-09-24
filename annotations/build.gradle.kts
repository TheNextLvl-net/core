plugins {
    id("java")
    id("maven-publish")
}

group = "net.thenextlvl.core"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
        repositories {
            maven {
                val branch = if (version.toString().contains("pre")) "snapshots" else "releases"
                url = uri("https://repo.thenextlvl.net/$branch")
                credentials {
                    username = extra["RELEASES_USER"].toString()
                    password = extra["RELEASES_PASSWORD"].toString()
                }
            }
        }
    }
}