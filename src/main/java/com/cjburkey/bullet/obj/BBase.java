package com.cjburkey.bullet.obj;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public abstract class BBase {
    
    public final int startLine;
    public final int endLine;
    public final int startCharPos;
    public final int endCharPos;
    
    public BBase(ParserRuleContext ctx) {
        startLine = ctx.start.getLine();
        startCharPos = ctx.start.getCharPositionInLine();
        endLine = ctx.stop.getLine();
        endCharPos = ctx.stop.getCharPositionInLine();
    }
    
}
