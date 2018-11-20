package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AOperator;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AUnaryOperator extends AExpression {
    
    public final AExpression expression;
    public final AOperator operator;
    
    public AUnaryOperator(AExpression expression, AOperator operator, BulletParser.UnaryOpContext ctx) {
        super(ctx);
        
        this.expression = expression;
        this.operator = operator;
    }
    
}
