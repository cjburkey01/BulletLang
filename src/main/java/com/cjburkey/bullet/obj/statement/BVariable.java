package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.IBScope;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BVariable extends BStatement {
    
    public final String name;
    public final String type;
    public BExpression value;
    
    public BVariable(IBScope parentScope, BulletParser.VariableDefContext ctx) {
        super(parentScope, ctx);
        
        this.name = p(ctx, 0);
        if (name == null) {
            throw new RuntimeException("Variable name cannot be null");
        }
        this.type = p(ctx, 1);
    }
    
    public String toString() {
        return String.format("Define variable \"%s\" of type \"%s\" = %s", name, type == null ? '?' : type, value == null ? "Null" : value);
    }
    
    private static String p(BulletParser.VariableDefContext ctx, int i) {
        return (ctx.IDENTIFIER().size() > i) ? ctx.IDENTIFIER(i).getText() : null;
    }
    
}
