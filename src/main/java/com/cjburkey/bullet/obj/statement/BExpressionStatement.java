package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.IBScope;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BExpressionStatement extends BStatement {
    
    public final BExpression expression;
    
    public BExpressionStatement(IBScope parentScope, BulletParser.ExpressionContext ctx) {
        super(parentScope, ctx);
        
        this.expression = new BExpression(ctx);
    }
    
    public String toString() {
        return "Execute expression: " + expression;
    }
    
}
