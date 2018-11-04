# BulletLang

#### Versions

* Source version: `0.0.1`
* Last release version: `N/A`

## About

A fast, efficient programming language with syntax as comfortable as Ruby and Java that compiles down to the ultra-fast and pretty-efficient [C language](https://en.wikipedia.org/wiki/C_(programming_language).

## Executing Compiler

Example:

`java -jar /path/BulletLangCompiler.jar --if /example/main.blt --of /example/output.c`

## Obtaining Source

You may use the GitHub download repository button to download this repository or clone it using git.

## Using the Source

To build/run the source code, [Maven](https://maven.apache.org/) isrequired on the system. Maven will automatically include the [Antlr4](http://www.antlr.org/) library, the Compiler/Parsergenerator for **BulletLang**, and other necessary libraries.

## Packaging to Jar from Source

In the cloned repository directory, run `mvn clean compile package` to include all the necessary libraries in the executable output Jar file.

## Running Source Without Packaging

##### This is probably *not* useful for anyone!!

In the cloned repository directoy, run `mvn clean compile exec:java -Dexec.args="--YOUR ARGS HERE--"` to execute the compiler without specifying a file to compile or building a jar first
