package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AName extends ABase {
    
    public final String identifier;
    
    public AName(AOperator operator, BulletParser.UnaryOpContext ctx) {
        super(ctx);
        
        this.identifier = operator.token;
    }
    
    public AName(AOperator operator, BulletParser.BinaryOpContext ctx) {
        super(ctx);
        
        this.identifier = operator.token;
    }
    
    public AName(String identifier, BulletParser.NameContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "Name:\n" + getIndent(indent + indent()) + identifier + '\n';
    }
    
}
