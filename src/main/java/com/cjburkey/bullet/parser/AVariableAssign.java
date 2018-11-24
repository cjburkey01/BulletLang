package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.function.AArgument;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

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
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(variableRef.verify());
        output.addAll(expression.verify());
        
        AReference reference = new AReference(variableRef, ctx);
        reference.setScopeParent(variableRef.getScope(), variableRef.getParent());
        output.addAll(reference.searchAndMerge());
        output.addAll(reference.verify());
        
        Optional<AVariable> variableDec = reference.resolveVariableReference();
        if (!variableDec.isPresent()) return output;
        
        if (variableDec.get() instanceof AVariableDec) {
            AVariableDec variableDec1 = (AVariableDec) variableDec.get();
            if (!variableDec1.resolveType().equals(expression.resolveType())) {
                output.add(onInvalidType(variableDec1));
            }
        } else {
            output.add(onArgument());
        }
        
        return output;
    }
    
    public BulletError onArgument() {
        return BulletError.format(ctx, "Cannot reassign argument value for \"%s\"", variableRef);
    }
    
    public BulletError onInvalidType(AVariableDec variableDec) {
        return BulletError.format(ctx, "Incorrect variable assignment type; expected: \"%s\" but found \"%s\"",
                variableDec.resolveType(), expression.resolveType());
    }
    
}
