package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AStatementReturn extends AStatement {
    
    public final AExpression expression;
    
    public AStatementReturn(AExpression expression, BulletParser.StatementReturnContext ctx) {
        super(ctx);
        
        this.expression = expression;
    }
    
}
