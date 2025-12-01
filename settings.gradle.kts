plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

rootProject.name = "core"
include("adapters")
include("files")
include("functions")
include("paper")
include("utils")
