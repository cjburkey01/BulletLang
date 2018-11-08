package com.cjburkey.bullet;

import com.cjburkey.bullet.visitor.ParserVisitor;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Created by CJ Burkey on 2018/11/04
 */
public class ErrorHandler extends ConsoleErrorListener {
    
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        Log.error("Syntax error on line {} at {}: {}", line, charPositionInLine, msg);
        ParserVisitor.stop = true;
    }
    
}
