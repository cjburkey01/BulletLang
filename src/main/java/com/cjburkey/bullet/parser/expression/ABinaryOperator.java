package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AOperator;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class ABinaryOperator extends AOperatorExpression {
    
    public final AExpression expressionB;
    
    public ABinaryOperator(AExpression expressionA, AExpression expressionB, AOperator operator, BulletParser.BinaryOpContext ctx) {
        super(expressionA, operator, ctx);
        
        this.expressionB = expressionB;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "BinaryOperator:\n" + expressionA.debug(indent + indent()) +
                expressionB.getFormattedDebug(indent + 2) + operator.debug(indent + indent());
    }
    
    public AReference getFunctionReference() {
        if (functionReference == null) {
            functionReference = new AReference(expressionA, expressionB, operator, (BulletParser.BinaryOpContext) ctx);
        }
        return functionReference;
    }
    
}
