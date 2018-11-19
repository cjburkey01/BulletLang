package com.cjburkey.bullet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

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
    public void compile(InputStream input, OutputStream output) {
        
    }
    
}
