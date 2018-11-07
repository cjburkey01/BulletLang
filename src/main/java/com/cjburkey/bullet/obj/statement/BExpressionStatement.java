package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BExpressionStatement extends BStatement {
    
    public final BExpression expression;
    
    public BExpressionStatement(BExpression expression, BulletParser.ExpressionContext ctx) {
        super(ctx);
        
        this.expression = expression;
    }
    
    public String toString() {
        return String.format("Execute expression: [%s]", expression);
    }
    
}
