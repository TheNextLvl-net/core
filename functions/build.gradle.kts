plugins {
    id("java")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

group = "net.thenextlvl.core"
version = "1.0.0"

dependencies {
    compileOnly(project(":annotations"))
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