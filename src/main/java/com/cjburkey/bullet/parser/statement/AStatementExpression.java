package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AStatementExpression extends AStatement {
    
    public final AExpression expression;
    
    public AStatementExpression(AExpression expression, BulletParser.StatementExpressionContext ctx) {
        super(ctx);
        
        this.expression = expression;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "StatementExpression:\n" + expression.debug(indent + indent());
    }
    
}
