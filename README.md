# BulletLang

A language as easy as Ruby and Java but as efficient as C, Go, C#, and more!

#### Versions

* Source version: `0.0.1`
* Last release version: `N/A`

## About

A fast, efficient programming language with syntax as comfortable as Ruby and Java that compiles down to the ultra-fast and pretty-efficient [C language](https://en.wikipedia.org/wiki/C_(programming_language)).


## Executing Compiler

Example:

`java -jar /path/bullet-lang-VERSION.jar --if /example/main.blt --of /example/output.c`

If you set an alias:

`bullet --if /example/main.blt --of /example/output.c`

## Obtaining Source

You may use the GitHub download repository button to download this repository or clone it using git.

## Using the Source

To build/run the source code, [Maven](https://maven.apache.org/) is required on the system. Maven will automatically include the [Antlr4](http://www.antlr.org/) library, the Compiler/Parser generator for **BulletLang**, and other necessary libraries.

## Packaging to Jar from Source

In the cloned repository directory, run `mvn clean compile inject:inject package` to include all the necessary libraries in the executable output Jar file.

* `clean` - cleans up any previous builds (it can be ommitted if this is the first execution of the `mvn` command in this diretory).
* `compile` - compiles the Antlr source files into Java and the java source files into their `.class` equivalents.
* `inject:inject` - injects the project information into the already-compiled Java files.
* `package` - Builds the class files and dependencies into a single executable Jar file.

## Packaging to EXE from Source

Packing to a `.exe` file is as easy as to a Jar file. You can generate a `.exe` file on any operating system supported by [Launch4j](http://launch4j.sourceforge.net/). The `build.exe` argument can be used to trigger the exe creation: `mvn clean compile inject:inject package -Dbuild.exe=true`

## Running Source Without Packaging

##### This is probably *not* useful for anyone!!

In the cloned repository directoy, run `mvn clean compile inject:inject exec:java -Dexec.args="--YOUR ARGS HERE--"` to execute the compiler without building a jar first
