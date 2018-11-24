package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AOperator;
import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.AReference;
import com.cjburkey.bullet.parser.ATypeDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
    
    public void settleChildren() {
        expressionB.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = super.searchAndMerge();
        output.addAll(expressionB.searchAndMerge());
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = super.verify();
        output.addAll(expressionB.verify());
        return output;
    }
    
    public AReference getFunctionReference() {
        if (functionReference == null) {
            functionReference = new AReference(expressionB, operator, (BulletParser.BinaryOpContext) ctx);
        }
        return functionReference;
    }
    
    // TODO
    public ATypeDec resolveType() {
        return null;
    }
    
}
