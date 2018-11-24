package com.cjburkey.bullet.parser;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/23
 */
public abstract class AVariable extends ABase {
    
    public final AName name;
    
    public AVariable(AName name, ParserRuleContext ctx) {
        super(ctx);
        
        this.name = name;
    }
    
    public abstract ATypeDec resolveType();
    
}