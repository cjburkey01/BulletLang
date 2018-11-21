package com.cjburkey.bullet;

import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Created by CJ Burkey on 2018/11/04
 */
@SuppressWarnings("WeakerAccess")
public class ErrorHandler extends ConsoleErrorListener {
    
    private static boolean errored;
    
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errored = true;
        Log.error("Syntax error on line {} at {}: {}", line, charPositionInLine, msg);
    }
    
    public static boolean hasErrored() {
        return errored;
    }
    
}
