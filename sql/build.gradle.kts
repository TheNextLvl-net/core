plugins {
    id("java")
}

group = "net.thenextlvl.core"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.28")
    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly(project(":annotations"))
    compileOnly(project(":api"))

    implementation(project(":utils"))

    annotationProcessor("org.projectlombok:lombok:1.18.28")
}