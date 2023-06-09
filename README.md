# Core
Core is a collection of Java libraries that provide various functionalities for different purposes.<br/>
It includes the following modules:

- [core-api](#core-api)
- [bukkit-core](#bukkit-core)
- [paper-core](#paper-core)
- [core-annotations](#core-annotations)
- [core-utils](#core-utils)
- [core-logger](#core-logger)

## core-api

The **core-api** module offers a comprehensive file API that facilitates editing and<br/>
loading of various file formats such as Gson, JSON, CSV, TSV, properties, shell<br/>
scripts, and plain text. Additionally, it provides an internationalization (i18n) API and<br/>
a placeholder API for convenient text manipulation. The **core-api** module also<br/>
includes functional interfaces like **TriConsumer**, **TriFunction**, and<br/>
**TriPredicate** to support advanced operations. Moreover, it provides a MySQL<br/>
API for seamless interaction with MySQL databases.

## bukkit-core

The **bukkit-core** module simplifies GUI development for Minecraft by offering a<br/>
user-friendly GUI API. It empowers developers to create and manage multiple<br/>
pages within the GUI effortlessly. Furthermore, it includes an item builder API that<br/>
enables easy creation and modification of items in Minecraft.

## paper-core

The **paper-core** is the same as the **bukkit-core** but made for paper development.<br/>
_(plans of using patches instead of copying the code)_

## core-annotations

The **core-annotations** module provides a set of annotations primarily focused<br/>
on nullability information. These annotations work in conjunction with package<br/>
information to enhance code clarity and maintainability. The available annotations are:

- @FieldsAreNonnullByDefault
- @FieldsAreNullableByDefault
- @MethodsReturnNonnullByDefault
- @MethodsReturnNullableByDefault
- @ParametersAreNonnullByDefault
- @ParametersAreNullableByDefault

By utilizing these annotations, developers can explicitly indicate whether fields,<br/>
methods, or parameters are expected to be null or non-null by default.

## core-logger

The **core-logger** module implements a logging mechanism based on the<br/>
popular SLF4J logging framework. It offers a custom text coloring feature to<br/>
enhance log readability and aesthetics.

## core-utils

The **core-utils** module includes a variety of utility functionalities to simplify<br/>
common tasks. It provides a custom properties object API for effortless creation and<br/>
manipulation of properties. Additionally, it offers a user-friendly file<br/>
downloader API to streamline file retrieval from remote sources. Lastly, it includes a<br/>
string utility API with functions for generating random strings, checking for<br/>
palindromes, and converting integers to Roman numerals.

## Installation

To use any of the Core modules in your Java project, you can include the desired<br/>
module as a dependency in your project's configuration file (e.g., **pom.xml** for<br/>
Maven projects or **build.gradle** for Gradle projects).

https://repo.thenextlvl.net/#/releases/net/thenextlvl/core
