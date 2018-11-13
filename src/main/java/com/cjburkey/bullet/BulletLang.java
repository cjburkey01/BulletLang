package com.cjburkey.bullet;

import com.cjburkey.bullet.antlr.BulletLexer;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.compiler.Compiler;
import com.cjburkey.bullet.obj.BFunction;
import com.cjburkey.bullet.obj.BNamespace;
import com.cjburkey.bullet.obj.BProgram;
import com.cjburkey.bullet.obj.classdef.BClass;
import com.cjburkey.bullet.obj.statement.BStatement;
import com.cjburkey.bullet.visitor.ParserVisitor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import static com.cjburkey.bullet.Log.*;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BulletLang {
    
    // INJECTED BY MAVEN-INJECTION-PLGUIN!
    // Make sure to run "mvn inject:inject" after
    // "mvn compile" when building from source
    public static String VERSION() {
        return null;
    }
    
    private static Input input;
    
    private static File startDirectory = null;
    private static File sourceDirectory = null;
    
    public static void main(String[] args) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> exception(e));
        input = Input.run(args);
        if (!input.help) {
            start();
        }
    }
    
    private static void start() throws IOException {
        // Validate input
        if (input == null) {
            throw new RuntimeException("Input not found");
        }
        
        if (input.sourceDirectory != null) {
            sourceDirectory = new File(input.sourceDirectory);
        }
        
        if (startDirectory == null) {
            startDirectory = new File(System.getProperty("user.home")).getAbsoluteFile();
        }
        if (sourceDirectory == null) {
            sourceDirectory = new File(startDirectory.getAbsolutePath());
        }
        BulletLang compiler = new BulletLang();
        
        debug = input.debug;
        
        // Debug prints
        if (input.debug && !input.printVersion) {
            debug("Debug enabled");
        }
        info("BulletLang Compiler Version \"{}\"", VERSION());
        if (input.printVersion) {
            return;
        }
        
        if (!input.valid || input.inputFile == null || input.outputFile == null) {
            error("Invalid input file");
            if (input.debug) {
                // TODO: REMOVE THIS TEST CODE
                debug("Performing test compile on resource \"/test.blt\" because no valid input file was found and debug is enabled");
                compiler.compile(BulletLang.class.getResourceAsStream("/test.blt"), null);
            }
            return;
        }
        
        // Clean paths
        input.inputFile = input.inputFile.replaceAll(Pattern.quote("\\"), "/");
        input.outputFile = input.outputFile.replaceAll(Pattern.quote("\\"), "/");
        if (!input.inputFile.endsWith(".blt")) {
            input.inputFile += ".blt";
        }
        if (!input.outputFile.endsWith(".c")) {
            input.outputFile += ".c";
        }
        File inputFile = new File(input.inputFile);
        
        // Check if input exists
        if (!inputFile.exists()) {
            error("File not found: \"{}\"", input.inputFile);
            return;
        }
        
        info("Compiling \"{}\" to \"{}\"", input.inputFile, input.outputFile);
        
        // Compile input file
        compiler.compile(new FileInputStream(inputFile), new FileOutputStream(new File(input.outputFile)));
    }
    
    @SuppressWarnings("unused")
    public void compile(InputStream input, OutputStream output) throws IOException {
        BProgram program = compileRaw(input, false, true);
        if (program != null && output == null) {
            error("Stopping compilation because there is no valid output file");
        }
    }
    
    public BProgram compileRaw(InputStream input, boolean skipVerify, boolean resolveRequirements) throws IOException {
        // Generate token stream
        BulletLexer lexer = new BulletLexer(CharStreams.fromStream(input));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        
        // Initialize parser
        BulletParser parser = new BulletParser(tokenStream);
        parser.setBuildParseTree(true);
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorHandler());
        
        // Begin parsing
        info("Parsing input");
        BProgram program = ParserVisitor.parse(parser);
        if (ParserVisitor.stop) {
            return null;
        }
        if (resolveRequirements) {
            boolean missing = false;
            List<File> reqd = new ArrayList<>();
            for (String requirement : program.requirements) {
                File requiredFile = new File(sourceDirectory, requirement);
                if (!requiredFile.exists()) {
                    requiredFile = new File(requirement);       // Try an absolute directory if it's not in the source directory
                    if (!requiredFile.exists()) {
                        error("Missing required file: \"{}\" in source directory or as an absolute path", requirement);
                        missing = true;
                        continue;
                    }
                }
                reqd.add(requiredFile);
            }
            if (missing) {
                error("Unable to proceed with compilation because required source file(s) could not be located");
                return null;
            }
            for (File req : reqd) {
                info("Merging \"{}\" into compilation", req.getAbsolutePath());
                BProgram reqdp = compileRaw(new FileInputStream(req), true, true);
                if (reqdp == null) {
                    return null;
                }
                // Merge required functions, classes, and namespaces
                program.functions.addAll(reqdp.functions);
                program.classes.addAll(reqdp.classes);
                info("Merged module into compilation");
            }
        }
        info("Finished parsing");
        if (skipVerify) {
            return program;
        }
        info("Compiling parsed program");
        Compiler compiler = new Compiler(program);
        if (compiler.compile()) {
            info("Finished compiling");
            debugSpam(program);
            return program;
        }
        return null;
    }
    
    public static void debugSpam(BProgram mainProgram) {
        if (!input.debug || ParserVisitor.stop) {
            return;
        }
        
        debug("Parsed program module {}", mainProgram);
        
        debug("  Namespaces ({}): ", mainProgram.namespaces.size());
        for (BNamespace namespace : mainProgram.namespaces) {
            debug("    {}", namespace);
        }
        
        debug("  Classes ({}): ", mainProgram.classes.size());
        for (BClass classDef : mainProgram.classes) {
            debug("    {}", classDef);
        }
        
        debug("  Functions ({}): ", mainProgram.functions.size());
        for (BFunction function : mainProgram.functions) {
            debug("    {}", function);
        }
        
        debug("  Statements ({}): ", mainProgram.scope.statements.size());
        for (BStatement statement : mainProgram.scope.statements) {
            debug("    {}", statement);
        }
    }
    
}
