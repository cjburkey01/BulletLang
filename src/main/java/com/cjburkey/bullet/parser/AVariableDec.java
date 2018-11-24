package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public class AVariableDec extends AVariable {
    
    public final AVariableRef variableRef;
    public final Optional<ATypeDec> typeDec;
    public final Optional<AExpression> expression;
    
    public AVariableDec(AVariableRef variableRef, Optional<ATypeDec> typeDec, Optional<AExpression> expression,
                        BulletParser.VariableDecContext ctx) {
        super(variableRef.name, ctx);
        
        this.variableRef = variableRef;
        this.typeDec = typeDec;
        this.expression = expression;
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("VariableDec:\n");
        
        output.append(variableRef.debug(indent + indent()));
        typeDec.ifPresent(aTypeDec -> output.append(aTypeDec.debug(indent + indent())));
        expression.ifPresent(aExpression -> output.append(aExpression.debug(indent + indent())));
        return output.toString();
    }
    
    public void settleChildren() {
        variableRef.setScopeParent(getScope(), this);
        IScopeContainer.makeChild(getScope(), this, typeDec);
        IScopeContainer.makeChild(getScope(), this, expression);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(variableRef.searchAndMerge());
        typeDec.ifPresent(aTypeDec -> output.addAll(aTypeDec.searchAndMerge()));
        expression.ifPresent(aExpression -> output.addAll(aExpression.searchAndMerge()));
        
        // Ignore statement variable declarations; they will shadow each other
        if (getScope().getVariableDecs().isPresent()) {
            for (AVariableDec variableDec : getScope().getVariableDecs().get()) {
                if (variableDec != this && variableDec.variableRef.equals(variableRef)) {
                    output.add(onDuplicate(variableDec));
                }
            }
        }
        
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = variableRef.verify();
        typeDec.ifPresent(aTypeDec -> output.addAll(aTypeDec.verify()));
        expression.ifPresent(aExpression -> output.addAll(aExpression.verify()));
        if (!typeDec.isPresent() && !expression.isPresent()) {
            output.add(new BulletError("Invalid variable declaration", ctx));
        }
        return output;
    }
    
    private BulletError onDuplicate(AVariableDec other) {
        return BulletError.format(ctx, "Variable of name \"%s\" has already been declared", other.variableRef.toString());
    }
    
    public ATypeDec resolveType() {
        return typeDec.orElseGet(() -> expression.map(AExpression::resolveType).orElse(null));
    }
    
}
