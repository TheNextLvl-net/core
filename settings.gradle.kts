plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

rootProject.name = "core"
include("adapters")
include("files")
include("functions")
include("i18n")
include("paper")
include("utils")
include("version-checker")
