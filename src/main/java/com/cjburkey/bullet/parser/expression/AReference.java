package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.AVariableRef;
import com.cjburkey.bullet.parser.function.AFuncParams;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class AReference extends AExpression {
    
    public boolean isFunctionRef;
    public final Optional<AExpression> expression;
    public final Optional<AName> name;
    public final Optional<AVariableRef> variableRef;
    public final Optional<AFuncParams> funcParams;
    
    public AReference(Optional<AExpression> expression, AName name, Optional<AFuncParams> funcParams,
                         BulletParser.FunctionReferenceContext ctx) {
        super(ctx);
        
        this.isFunctionRef = true;
        this.expression = expression;
        this.name = Optional.of(name);
        this.variableRef = Optional.empty();
        this.funcParams = funcParams;
    }
    
    public AReference(Optional<AExpression> expression, AVariableRef variableRef, BulletParser.ReferenceContext ctx) {
        super(ctx);
        
        this.isFunctionRef = false;
        this.expression = expression;
        this.name = Optional.empty();
        this.variableRef = Optional.of(variableRef);
        this.funcParams = Optional.empty();
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Reference:\n");
        output.append(getIndent(indent + indent()));
        output.append("IsFunctionRef:\n");
        output.append(getIndent(indent + indent() * 2));
        output.append(isFunctionRef);
        output.append('\n');
        
        expression.ifPresent(aExpression -> output.append(aExpression.debug(indent + indent())));
        name.ifPresent(aName -> output.append(aName.debug(indent + indent())));
        variableRef.ifPresent(aVariableRef -> output.append(aVariableRef.debug(indent + indent())));
        funcParams.ifPresent(aFuncParams -> output.append(aFuncParams.debug(indent + indent())));
        return output.toString();
    }
    
}
