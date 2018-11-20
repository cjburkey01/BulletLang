package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AString extends AExpression {
    
    public final String string;
    
    public AString(String string, BulletParser.StringContext ctx) {
        super(ctx);
        
        this.string = string;
    }
    
    public AString(String string, BulletParser.LiteralStringContext ctx) {
        super(ctx);
        
        this.string = string;
    }
    
}
