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
    
}
