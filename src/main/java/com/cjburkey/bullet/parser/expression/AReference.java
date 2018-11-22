package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.AOperator;
import com.cjburkey.bullet.parser.AVariableRef;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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
    public final Optional<AExprList> funcParams;
    
    // Create a function reference to <expression>.<operator>()
    AReference(AExpression expression, AOperator operator, BulletParser.UnaryOpContext ctx) {
        super(ctx);
        
        this.isFunctionRef = true;
        this.expression = Optional.ofNullable(expression);
        this.name = Optional.of(new AName(operator, ctx));
        this.variableRef = Optional.empty();
        this.funcParams = Optional.empty();
    }
    
    // Create a function reference to <expressionA>.<operator>(<expressionB>)
    AReference(AExpression expressionA, AExpression expressionB, AOperator operator, BulletParser.BinaryOpContext ctx) {
        super(ctx);
        
        this.isFunctionRef = true;
        this.expression = Optional.ofNullable(expressionA);
        this.name = Optional.of(new AName(operator, ctx));
        this.variableRef = Optional.empty();
        this.funcParams = Optional.of(new AExprList(ctx));
        
        this.funcParams.get().expressions.add(expressionB);
    }
    
    public AReference(Optional<AExpression> expression, AOperator operator, Optional<AExprList> funcParams,
                         BulletParser.FunctionReferenceContext ctx) {
        super(ctx);
        
        this.isFunctionRef = true;
        this.expression = expression;
        this.name = Optional.of(new AName(operator, ctx));
        this.variableRef = Optional.empty();
        this.funcParams = funcParams;
    }
    
    public AReference(Optional<AExpression> expression, AName name, Optional<AExprList> funcParams,
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
        funcParams.ifPresent(aExprList -> output.append(aExprList.debug(indent + indent())));
        return output.toString();
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        ObjectArrayList<BulletVerifyError> output = expression.map(AExpression::verify).orElseGet(ObjectArrayList::new);
        name.ifPresent(aName -> output.addAll(aName.verify()));
        variableRef.ifPresent(aVariableRef -> output.addAll(aVariableRef.verify()));
        funcParams.ifPresent(aExprList -> output.addAll(aExprList.verify()));
        return output;
    }
    
}
