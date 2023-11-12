plugins {
    id("java")
    id("maven-publish")
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
            username = extra["RELEASES_USER"].toString()
            password = extra["RELEASES_PASSWORD"].toString()
        }
    }
}