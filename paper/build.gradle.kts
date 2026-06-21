plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    withSourcesJar()
    withJavadocJar()
}

tasks.compileJava {
    options.release.set(21)
}

configurations.compileClasspath {
    attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 25)
}

group = "net.thenextlvl.core"
version = "3.0.0-pre5"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(listOf("--add-reads", "core.paper=ALL-UNNAMED"))
}

tasks.withType<Test>().configureEach {
    jvmArgs("--add-reads", "core.paper=ALL-UNNAMED")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--add-reads", "core.paper=ALL-UNNAMED")
}

tasks.withType<Javadoc>().configureEach {
    val options = options as StandardJavadocDocletOptions
    options.tags("apiNote:a:API Note:", "implSpec:a:Implementation Requirements:")
    options.addStringOption("-add-reads", "core.paper=ALL-UNNAMED")
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