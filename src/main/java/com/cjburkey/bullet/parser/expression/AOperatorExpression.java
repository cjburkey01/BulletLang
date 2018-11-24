package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AOperator;
import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.AReference;
import com.cjburkey.bullet.parser.ATypeDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/21
 */
@SuppressWarnings("WeakerAccess")
public abstract class AOperatorExpression extends AExpression {
    
    public final AExpression expressionA;
    public final AOperator operator;
    
    protected AReference functionReference;
    
    public AOperatorExpression(AExpression expressionA, AOperator operator, BulletParser.UnaryOpContext ctx) {
        super(ctx);
        
        this.expressionA = expressionA;
        this.operator = operator;
    }
    
    public AOperatorExpression(AExpression expressionA, AOperator operator, BulletParser.BinaryOpContext ctx) {
        super(ctx);
        
        this.expressionA = expressionA;
        this.operator = operator;
    }
    
    public AReference getFunctionReference() {
        if (functionReference == null) {
            functionReference = new AReference(operator, (BulletParser.UnaryOpContext) ctx);
        }
        return functionReference;
    }
    
    public void settleChildren() {
        expressionA.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return expressionA.searchAndMerge();
    }
    
    public ObjectArrayList<BulletError> verify() {
        return expressionA.verify();
    }
    
    // TODO
    public ATypeDec resolveType() {
        return null;
    }
    
}
