package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class ABoolean extends AExpression {
    
    public final boolean bool;
    
    public ABoolean(boolean bool, BulletParser.BooleanContext ctx) {
        super(ctx);
        
        this.bool = bool;
    }
    
}
