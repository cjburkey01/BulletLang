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

## How Compilation Works

The compiler runs in discrete steps, each relying on the step(s) prior to it. The steps are as follows:
* `Parsing` - Reads the program into memory to allow manipulation, verification and compilation.
* `Settling` - Organizes the memory map of the data more explicitly to maintain structures present in the input source.
* `Merging and Searching` - Allows certain structures, such as classes and namespaces, to combine with other classes/namespaces of the same name (and for classes, of the same parent classes).
* `Verifying` - Verifies that there will not likely be any compilation errors; this step is the most complicated of all as it requires full verification of the source, resolution of implicitly defined types and references, and validity checking of the result of the execution.
* `Compilation` - Converts the memory map of the source into a (hopefully) valid _C_ file, which may then be compiled into binary natives. Though, the _C_ file itself can be shared across platforms to allow for a cross-platform intermediate compilation step.

## Obtaining Source

You may use the GitHub download repository button to download this repository or clone it using git.

## Using the Source

To build/run the source code, [Maven](https://maven.apache.org/) is required on the system. Maven will automatically include the [Antlr4](http://www.antlr.org/) library, the Compiler/Parser generator for **BulletLang**, and other necessary libraries.

## Packaging to Jar from Source

In the cloned repository directory, run `mvn clean package` to include all the necessary libraries in the executable output Jar file.

## Packaging to EXE from Source

Packing to a `.exe` file is as easy as to a Jar file. You can generate a `.exe` file on any operating system supported by [Launch4j](http://launch4j.sourceforge.net/). The `build.exe` argument can be used to trigger the exe creation: `mvn clean package -Dbuild.exe=true`

## Running Source Without Packaging

**This is probably _not_ useful for anyone!!**

In the cloned repository directoy, run `mvn clean compile inject:inject exec:java -Dexec.args="--YOUR ARGS HERE--"` to execute the compiler without building a jar first
