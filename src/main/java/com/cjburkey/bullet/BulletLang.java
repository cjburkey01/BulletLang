package com.cjburkey.bullet;

import com.cjburkey.bullet.antlr.BulletLexer;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BFunction;
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
    public static final String VERSION() {
        return null;
    }
    
    private static Input input;
    
    private static File startDirectory;
    private static File sourceDirectory;
    
    public static void main(String[] args) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> exception(e));
        input = Input.run(args);
        start();
    }
    
    private static void start() throws IOException {
        // Validate input
        if (input == null) {
            throw new RuntimeException("Input not found");
        }
        
        // Debug prints
        if (input.debug && !input.printVersion) {
            debug("Debug enabled");
        }
        if (input.printVersion || input.debug) {
            info("BulletLang Compiler Version \"{}\"", VERSION());
        }
        if (input.printVersion) {
            return;
        }
        
        if (!input.valid || input.inputFile == null || input.outputFile == null) {
            // TODO: REMOVE THIS TEST CODE
            debug("Performing test compile on resource \"/test.blt\"");
            compile(BulletLang.class.getResourceAsStream("/test.blt"), null);
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
        compile(new FileInputStream(inputFile), new FileOutputStream(new File(input.outputFile)));
    }
    
    @SuppressWarnings("unused")
    private static void compile(InputStream input, OutputStream output) throws IOException {
        if (startDirectory == null) {
            startDirectory = new File(System.getProperty("user.home")).getAbsoluteFile();
        }
        if (sourceDirectory == null) {
            sourceDirectory = new File(startDirectory.getAbsolutePath());
        }
        
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
        BProgram mainProgram = ParserVisitor.parse(parser);
        if (!ParserVisitor.stop) {
            info("Finished parsing");
        }
    }
    
    public static void debugPrint(BProgram mainProgram) {
        if (!input.debug || ParserVisitor.stop) {
            return;
        }
        
        debug("Parsed program module {}", mainProgram);
        
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
