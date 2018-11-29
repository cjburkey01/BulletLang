package com.cjburkey.bullet.parser.variable;

import com.cjburkey.bullet.BulletLang;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AReference;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AVariableAssign extends ABase {
    
    public final AReference reference;
    public final AExpression expression;
    
    public AVariableAssign(AReference reference, AExpression expression, BulletParser.VariableAssignContext ctx) {
        super(ctx);
        
        this.reference = reference;
        this.expression = expression;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "VariableAssign:\n" + reference.debug(indent + indent()) +
                expression.debug(indent + indent());
    }
    
    public void settleChildren() {
        reference.setScopeParent(getScope(), this);
        expression.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(reference.searchAndMerge());
        output.addAll(expression.searchAndMerge());
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(reference.verify());
        output.addAll(expression.verify());
        
        Optional<AVariable> variableDec = reference.resolveVariableReference();
        if (!variableDec.isPresent()) return output;
        
        if (variableDec.get() instanceof AVariableDec) {
            AVariableDec variableDec1 = (AVariableDec) variableDec.get();
            if (!variableDec1.resolveType().equals(expression.resolveType())) output.add(onInvalidType(variableDec1));
        } else output.add(onArgument());
        
        return output;
    }
    
    public BulletError onArgument() {
        return BulletError.format(ctx, "Cannot reassign argument value for \"%s\"", reference);
    }
    
    public BulletError onInvalidType(AVariableDec variableDec) {
        return BulletError.format(ctx, "Incorrect variable assignment type for variable \"%s\"; expected: \"%s\" but found \"%s\"",
                variableDec.variableRef,
                variableDec.resolveType().map(typeDec -> typeDec.typeWhole.toString()).orElse("?"),
                expression.resolveType().map(typeDec -> typeDec.typeWhole.toString()).orElse("?"));
    }
    
}
