# Core

Core is a collection of Java libraries that provide various functionalities for different purposes. It includes the
following modules:

- [annotations](#annotations)
- [files](#files)
- [functions](#functions)
- [i18n](#i18n)
- [nbt](#nbt)
- [adapters](#adapters)
- [paper](#paper)
- [utils](#utils)

## Annotations

The **annotations** module provides a set of annotations primarily focused on nullability and api information. These
annotations work in conjunction with package information to enhance code clarity and maintainability. The available
annotations are:

- **@TypesAreNotNullByDefault** _Indicates that types cannot be null by default_
- **@TypesAreNullableByDefault** _Indicates that types may be null by default_
- **@FieldsAreNotNullByDefault** _Indicates that fields cannot be null by default_
- **@FieldsAreNullableByDefault** _Indicates that fields may be null by default_
- **@MethodsReturnNotNullByDefault** _Indicates that methods cannot return null by default_
- **@MethodsReturnNullableByDefault** _Indicates that methods may return null by default_
- **@ParametersAreNullableByDefault** _Indicates that parameters cannot be null by default_
- **@ParametersAreNotNullByDefault** _Indicates that parameters may be null by default_
- **@ClassesAreOverrideOnlyByDefault** _Indicates that classes are override-only by default_
- **@MethodsAreOverrideOnlyByDefault** _Indicates that methods are override-only by default_

By utilizing these annotations, developers can explicitly indicate whether fields, methods, parameters or Types are
expected to be null or not-null by default. The nullability can be overridden with explicit annotations.

## Files

The **files** module offers a comprehensive file API that facilitates editing and loading of various file formats such
as Gson, JSON, CSV, TSV, properties, shell scripts, and plain text.

## Functions

The **functions** module provides a set of functional interfaces that extend Java's standard functional interfaces to
handle three objects. These interfaces include:

- **TriPredicate** _A predicate that takes three input parameters and returns a boolean result._
- **TriFunction** _A function that takes three input parameters and produces a result._
- **TriConsumer** _A consumer that takes three input parameters and performs some operation without returning a result._

These interfaces are designed to offer functionality for handling three objects simultaneously, similar to Java's
BiPredicate, BiFunction, and BiConsumer. They can be particularly useful in scenarios where operations involve
interactions between three entities.

## i18n

The **i18n** module serves as an internationalization (i18n) library for Java applications. It employs property files,
similar to Java's ResourceBundle, but enhances the functionality with great features.

- **Language Fallback** _The module supports language fallback, ensuring that if a specific translation is not available
  for a chosen language, it falls back to a default language._

- **Custom Encodings Support** _It provides support for custom encodings, allowing developers to specify encoding
  preferences for language files._

- **Kyori Components Integration** _The module is built on Kyori components, leveraging the MiniMessage library. This
  enhances the handling and formatting of internationalized messages._

- **Missing Translations Handling** _The module automatically merges missing translations from the original resource,
  ensuring that if a translation is absent in a specific language, it falls back to the original resource._

- **Dropping Non-existing Translations** _Non-existing translations are dropped, streamlining the language resource
  files by removing unnecessary entries._

This library is designed to simplify the process of internationalizing Java applications, making it easy for
developers to manage translations, handle fallback scenarios, customize encoding preferences, and efficiently handle
missing and non-existing translations.

## NBT

The **nbt** module introduces a robust and efficient API for working with NBT (Named Binary Tag) data in Java
applications. NBT is a binary serialization format used in Minecraft to store structured data, such as items, entities,
and tile entities. With the core-nbt module, you can seamlessly read and write NBT data, manipulate its contents, and
integrate it into your projects.

## Adapters

The **adapters** module within the Core collection is dedicated to providing GsonAdapters for the de/serialization of
various elements commonly used in Minecraft development. These GsonAdapters cover the de/serialization of:

- **Items**
- **Keys**
- **Locations**
- **Materials**
- **Offline Players**
- **Player Profiles**
- **Plugins**
- **Worlds**

These adapters facilitate the smooth conversion of Minecraft-related elements into a format suitable for users.

## Paper

The **paper** module simplifies GUI development for Minecraft by offering a user-friendly GUI API. It empowers
developers to create and manage multiple pages within the GUI effortlessly. Furthermore, it includes an item builder API
that enables easy creation and modification of items in Minecraft. We also offer a very simple and intuitive Sidebar
API.

## Utils

The **utils** module includes a variety of utility functionalities to simplify common tasks. It provides a custom
properties object API for effortless creation and manipulation of properties. Additionally, it offers a user-friendly
file downloader API to streamline file retrieval from remote sources. Lastly, it includes a string utility API with
functions for generating random strings, checking for palindromes, and converting numbers to Roman numerals.

## Usage

To use any of the Core modules in your Java project, you can include the desired module as a dependency in your
project's configuration file (e.g., **pom.xml** for Maven projects or **build.gradle** for Gradle projects).

https://repo.thenextlvl.net/#/releases/net/thenextlvl/core
