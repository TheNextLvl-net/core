plugins {
    id("java")
}

group = "net.thenextlvl.core"
version = "1.0.0"

dependencies {
    compileOnly(project(":annotations"))
}