package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AOperator;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class ABinaryOperator extends AExpression {
    
    public final AExpression expressionA;
    public final AExpression expressionB;
    public final AOperator operator;
    
    public ABinaryOperator(AExpression expressionA, AExpression expressionB, AOperator operator, BulletParser.BinaryOpContext ctx) {
        super(ctx);
        
        this.expressionA = expressionA;
        this.expressionB = expressionB;
        this.operator = operator;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "BinaryOperator:\n" + expressionA.debug(indent + indent()) +
                expressionB.getFormattedDebug(indent + 2) + operator.debug(indent + indent());
    }
    
}
