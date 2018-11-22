package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public class AVariableDec extends ABase {
    
    public final AVariableRef variableRef;
    public final Optional<ATypeDec> typeDec;
    public final Optional<AExpression> expression;
    
    public AVariableDec(AVariableRef variableRef, Optional<ATypeDec> typeDec, Optional<AExpression> expression,
                        BulletParser.VariableDecContext ctx) {
        super(ctx);
        
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
    
    public ObjectArrayList<BulletVerifyError> verify() {
        ObjectArrayList<BulletVerifyError> output = variableRef.verify();
        typeDec.ifPresent(aTypeDec -> output.addAll(aTypeDec.verify()));
        expression.ifPresent(aExpression -> output.addAll(aExpression.verify()));
        if (!typeDec.isPresent() && !expression.isPresent()) {
            output.add(new BulletVerifyError("Invalid variable declaration", ctx));
        }
        return output;
    }
    
}
