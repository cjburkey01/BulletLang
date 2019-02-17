package com.cjburkey.bullet;

import com.cjburkey.bullet.antlr.BulletLangLexer;
import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.component.Program;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BulletCompiler {

    private final String rawText;

    private BulletCompiler(String rawText) {
        this.rawText = Objects.requireNonNull(rawText);
    }

    public Program parse() {
        BulletLangLexer lexer = new BulletLangLexer(CharStreams.fromString(rawText));
        BulletLangParser parser = new BulletLangParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorHandler());

        return new Program.Visitor()
                .visit(parser.program())
                .orElse(null);
    }

    public static Program create(String rawText) {
        BulletCompiler compiler = new BulletCompiler(rawText);
        return compiler.parse();
    }

    public static Program create(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            return create(reader.lines().collect(Collectors.joining(System.lineSeparator())));
        }
    }

    // -- STATIC -- //

    public static void main(String[] args) {
        Log.debug("Hello world!");
        Log.debug("Testing on: \"test.blt\"");

        try {
            Program program = create(BulletCompiler.class.getClassLoader().getResourceAsStream("test.blt"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
