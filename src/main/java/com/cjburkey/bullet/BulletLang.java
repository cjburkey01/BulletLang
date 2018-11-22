package com.cjburkey.bullet;

import com.cjburkey.bullet.antlr.BulletLexer;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.program.AProgram;
import com.cjburkey.bullet.verify.BulletVerifyError;
import com.cjburkey.bullet.visitor.ParserVisitor;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
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
    // "mvn compile" when building from source!
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
                debug("Performing test compile on resource \"/test2.blt\" because no valid input file was found and debug is enabled");
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
        // Initialize parser
        BulletParser parser = buildParser(buildLexer(input));
        parser.setBuildParseTree(true);                 // Allows us to visit the parse tree and build our own program structure
        parser.removeErrorListeners();                  // Clear the default error listener
        parser.addErrorListener(new ErrorHandler());    // Register our custom error listener
        
        // Begin parsing
        info("Parsing input");
        Optional<AProgram> program = ParserVisitor.parseProgram(parser.program());
        if (!program.isPresent() || ErrorHandler.hasErrored()) {
            error("Failed to parse input");
            return;
        }
        if (debug) {
            debugPrint(program.get());
        }
        
        info("Settling");
        program.get().settleChildren();
        
        info("Verifying");
        ObjectArrayList<BulletVerifyError> errors = program.get().verify();
        if (!errors.isEmpty()) {
            for (BulletVerifyError error : errors) {
                error.print(Log::error);
            }
            error("Failed to verify");
            return;
        }
        
        info("Compiling");
    }
    
    private void debugPrint(AProgram program) {
        info("Debug print...");
        System.out.println();
        String out = program.debug(0);
        while (out.endsWith("\n")) {
            out = out.substring(0, out.length() - 1);
        }
        System.out.println(out);
        System.out.println();
        info("Finished print");
    }
    
    public static BulletLexer buildLexer(InputStream input) throws IOException {
        return new BulletLexer(CharStreams.fromStream(input, StandardCharsets.UTF_8));
    }
    
    public static BulletLexer buildLexer(String input) {
        return new BulletLexer(CharStreams.fromString(input));
    }
    
    public static BulletParser buildParser(BulletLexer lexer) {
        return new BulletParser(new CommonTokenStream(lexer));
    }
    
    public static BulletParser buildQuickParser(String input) {
        return buildParser(buildLexer(input));
    }
    
}
