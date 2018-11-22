package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "VariableAssign:\n" + variableRef.debug(indent + indent()) +
                expression.debug(indent + indent());
    }
    
    public void settleChildren() {
        variableRef.setScopeParent(getScope(), this);
        expression.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(variableRef.searchAndMerge());
        output.addAll(expression.searchAndMerge());
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = variableRef.verify();
        output.addAll(expression.verify());
        return output;
    }
    
}
