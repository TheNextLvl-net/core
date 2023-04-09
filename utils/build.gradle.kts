plugins {
    id("java")
}

group = "net.thenextlvl.core"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("org.jetbrains:annotations:24.0.0")

    implementation(project(":annotations"))

    annotationProcessor(project(":annotations"))
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}