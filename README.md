# Core
Core is a collection of Java libraries that provide various functionalities for different purposes.<br/>
It includes the following modules:

- [core-api](#core-api)
- [paper-core](#paper-core)
- [core-annotations](#core-annotations)
- [core-utils](#core-utils)
- [core-nbt](#core-nbt)

## core-api

The **core-api** module offers a comprehensive file API that facilitates editing and<br/>
loading of various file formats such as Gson, JSON, CSV, TSV, properties, shell<br/>
scripts, and plain text. Additionally, it provides an internationalization (i18n) API and<br/>
a placeholder API for convenient text manipulation. The **core-api** module also<br/>
includes functional interfaces like **TriConsumer**, **TriFunction**, and<br/>
**TriPredicate** to support advanced operations. Moreover, it provides a MySQL<br/>
API for seamless interaction with MySQL databases.

## paper-core

The **paper-core** module simplifies GUI development for Minecraft by offering a<br/>
user-friendly GUI API. It empowers developers to create and manage multiple<br/>
pages within the GUI effortlessly. Furthermore, it includes an item builder API that<br/>
enables easy creation and modification of items in Minecraft.<br/>
Also we offer a very simple and intuitive Sidebar API.

## core-annotations

The **core-annotations** module provides a set of annotations primarily focused<br/>
on nullability and api information. These annotations work in conjunction with package<br/>
information to enhance code clarity and maintainability. The available annotations are:

- @FieldsAreNonnullByDefault
- @FieldsAreNullableByDefault
- @MethodsReturnNonnullByDefault
- @MethodsReturnNullableByDefault
- @ParametersAreNonnullByDefault
- @ParametersAreNullableByDefault
- @ClassesAreOverrideOnlyByDefault
- @MethodsAreOverrideOnlyByDefault

By utilizing these annotations, developers can explicitly indicate whether fields,<br/>
methods, or parameters are expected to be null or non-null by default.

## core-utils

The **core-utils** module includes a variety of utility functionalities to simplify<br/>
common tasks. It provides a custom properties object API for effortless creation and<br/>
manipulation of properties. Additionally, it offers a user-friendly file<br/>
downloader API to streamline file retrieval from remote sources. Lastly, it includes a<br/>
string utility API with functions for generating random strings, checking for<br/>
palindromes, and converting integers to Roman numerals.

## core-nbt

The **core-nbt** module introduces a robust and efficient API for working with NBT (Named<br/>
Binary Tag) data in Java applications. NBT is a binary serialization format used in Minecraft<br/>
to store structured data, such as items, entities, and tile entities. With the core-nbt module,<br/>
you can seamlessly read and write NBT data, manipulate its contents, and integrate it into<br/>
your projects.

## Installation

To use any of the Core modules in your Java project, you can include the desired<br/>
module as a dependency in your project's configuration file (e.g., **pom.xml** for<br/>
Maven projects or **build.gradle** for Gradle projects).

https://repo.thenextlvl.net/#/releases/net/thenextlvl/core
