package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AVariableAssign extends ABase {
    
    public final AVariableRef variableRef;
    public final AExpression expression;
    
    public AVariableAssign(AVariableRef variableRef, AExpression expression, BulletParser.VariableAssignContext ctx) {
        super(ctx);
        
        this.variableRef = variableRef;
        this.expression = expression;
    }
    
}
