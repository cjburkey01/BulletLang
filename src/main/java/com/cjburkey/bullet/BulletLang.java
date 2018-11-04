package com.cjburkey.bullet;

import com.cjburkey.bullet.antlr.BulletLexer;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.listener.BulletMainListener;
import com.cjburkey.bullet.obj.BFunction;
import com.cjburkey.bullet.obj.BProgram;
import com.cjburkey.bullet.obj.statement.BArgument;
import com.cjburkey.bullet.obj.statement.BExpressionStatement;
import com.cjburkey.bullet.obj.statement.BIfStatement;
import com.cjburkey.bullet.obj.statement.BStatement;
import com.cjburkey.bullet.obj.statement.BVariable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import static com.cjburkey.bullet.Log.*;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BulletLang {
    
    private static Input input;
    
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
        if (input.debug) {
            debug("Debug enabled");
        }
        if (!input.valid || input.inputFile == null || input.outputFile == null) {
            // TODO: REMOVE THIS TEST CODE
            info("Performing test compile on resource \"/test.blt\"");
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
        // Generate token stream
        BulletLexer lexer = new BulletLexer(CharStreams.fromStream(input));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        
        // Initialize parser
        BulletParser parser = new BulletParser(tokenStream);
        parser.setBuildParseTree(true);
        
        // Begin parsing
        ParseTree parseTree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new BulletMainListener(), parseTree);
        
        if (BulletLang.input.debug) {
            debugPrint();
        }
    }
    
    private static void debugPrint() {
        while (!BulletMainListener.programs.empty()) {
            BProgram program = BulletMainListener.programs.pop();
            debug("Program in namespace \"{}\" has {} functions and {} statements:", program.namespace, program.functions.size(), program.statements.size());
            debug("  Functions:");
            for (BFunction function : program.functions) {
                debug("    Define function \"{}\"(Arguments: {}) returns \"{}\" and runs {}", function.name, Arrays.toString(function.arguments.toArray(new BArgument[0])), function.type, Arrays.toString(function.statements.toArray(new BStatement[0])));
            }
            debug("  Statements:");
            for (BStatement statement : program.statements) {
                if (statement instanceof BVariable) {
                    BVariable variable = (BVariable) statement;
                    debug("    {}", variable);
                }
                if (statement instanceof BIfStatement) {
                    BIfStatement ifStatement = (BIfStatement) statement;
                    if (ifStatement.isElse) {
                        debug("    Else if \"{}\" then {}", ifStatement.condition, Arrays.toString(ifStatement.statements.toArray(new BStatement[0])));
                    } else {
                        debug("    If \"{}\" then {}", ifStatement.condition, Arrays.toString(ifStatement.statements.toArray(new BStatement[0])));
                    }
                }
                if (statement instanceof BExpressionStatement) {
                    BExpressionStatement expressionStatement = (BExpressionStatement) statement;
                    debug("    Execute expression: \"{}\"", expressionStatement.expression);
                }
            }
        }
    }
    
}
