# BulletLang [![Build Status](https://travis-ci.com/cjburkey01/BulletLang.svg?branch=master)](https://travis-ci.com/cjburkey01/BulletLang)

A language as easy as Ruby and Java but as efficient as C, Go, C#, and more!

#### Versions

* Source version: `0.0.1`
* Last release version: `N/A`

## About

A fast, efficient programming language with syntax as comfortable as Ruby and Java that compiles down to the ultra-fast and pretty-efficient [C language](https://en.wikipedia.org/wiki/C_(programming_language)).


## Executing Compiler

Example:

**TODO**

## How Compilation Works

**TODO**

## Obtaining Source

You may use the GitHub download repository button to download this repository or clone it using git.

## Using the Source

To build/run the source code, [Maven](https://maven.apache.org/) is required on the system. Maven will automatically include the [Antlr4](http://www.antlr.org/) library, the Parser generator for **BulletLang**, and other necessary libraries.

## Packaging to Jar from Source

In the cloned repository directory, run `mvn clean package` to include all the necessary libraries in the executable output Jar file.

## Packaging to EXE from Source

Packing to a `.exe` file is as easy as to a Jar file. You can generate a `.exe` file on any operating system supported by [Launch4j](http://launch4j.sourceforge.net/). The `build.exe` argument can be used to trigger the exe creation: `mvn clean package -Dbuild.exe=true`

## Running Source Without Packaging

**This is probably _not_ useful for anyone!!**

In the cloned repository directoy, run `mvn clean compile inject:inject exec:java -Dexec.args="--YOUR ARGS HERE--"` to execute the compiler without building a jar first
