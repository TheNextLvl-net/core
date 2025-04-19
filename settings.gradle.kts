plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.10.0")
}

rootProject.name = "core"
include("adapters")
include("files")
include("functions")
include("i18n")
include("nbt")
include("paper")
include("utils")
include("version-checker")
