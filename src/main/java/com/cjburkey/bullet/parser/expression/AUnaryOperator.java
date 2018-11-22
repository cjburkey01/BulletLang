package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AOperator;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AUnaryOperator extends AOperatorExpression {
    
    public AUnaryOperator(AExpression expression, AOperator operator, BulletParser.UnaryOpContext ctx) {
        super(expression, operator, ctx);
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "UnaryOperator:\n" + expressionA.debug(indent + indent()) + operator.debug(indent + indent());
    }
    
    public ObjectArrayList<BulletError> verify() {
        return super.verify();
    }
    
}
