package com.cjburkey.bullet.parser;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public abstract class ABase {
    
    public final ParserRuleContext ctx;
    
    public ABase(ParserRuleContext ctx) {
        this.ctx = ctx;
    }
    
    public final String debug(int indent) {
        String output = getFormattedDebug(indent);
        if (!output.endsWith("\n")) {
            output += '\n';
        }
        return output;
    }
    
    public abstract String getFormattedDebug(int indent);
    
    public static String getIndent(int amt) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < amt; i ++) output.append(i % indent() == 0 ? '|' : ' ');
        return output.toString();
    }
    
    public static int indent() {
        return 2;
    }
    
}
