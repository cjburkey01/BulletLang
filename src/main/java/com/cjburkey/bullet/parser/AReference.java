package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.parser.expression.AParentChild;
import com.cjburkey.bullet.parser.function.AArgument;
import com.cjburkey.bullet.parser.function.AArguments;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class AReference extends ABase {
    
    // When this is false, it could be a function, but it could also not be
    public boolean isUnambiguousFunctionRef;
    
    public Optional<AExpression> refParent = Optional.empty();
    public final Optional<AName> name;
    public final Optional<AVariableRef> variableRef;
    public final Optional<AExprList> exprList;
    
    // Create a function reference to <expression>.<operator>()
    public AReference(AOperator operator, BulletParser.UnaryOpContext ctx) {
        super(ctx);
        
        this.isUnambiguousFunctionRef = true;
        this.name = Optional.of(new AName(operator, ctx));
        this.variableRef = Optional.empty();
        this.exprList = Optional.empty();
    }
    
    // Create a function reference to <expressionA>.<operator>(<expressionB>)
    public AReference(AExpression expressionB, AOperator operator, BulletParser.BinaryOpContext ctx) {
        super(ctx);
        
        this.isUnambiguousFunctionRef = true;
        this.name = Optional.of(new AName(operator, ctx));
        this.variableRef = Optional.empty();
        this.exprList = Optional.of(new AExprList(ctx));
        
        this.exprList.get().expressions.add(expressionB);
    }
    
    public AReference(AOperator operator, Optional<AExprList> exprList,
                         BulletParser.FunctionReferenceContext ctx) {
        super(ctx);
        
        this.isUnambiguousFunctionRef = true;
        this.name = Optional.of(new AName(operator, ctx));
        this.variableRef = Optional.empty();
        this.exprList = exprList;
    }
    
    public AReference(AName name, Optional<AExprList> exprList,
                         BulletParser.FunctionReferenceContext ctx) {
        super(ctx);
        
        this.isUnambiguousFunctionRef = true;
        this.name = Optional.of(name);
        this.variableRef = Optional.empty();
        this.exprList = exprList;
    }
    
    public AReference(AVariableRef variableRef, ParserRuleContext ctx) {
        super(ctx);
        
        this.isUnambiguousFunctionRef = false;
        this.name = Optional.empty();
        this.variableRef = Optional.of(variableRef);
        this.exprList = Optional.empty();
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Reference:\n");
        output.append(getIndent(indent + indent()));
        output.append("IsFunctionRef:\n");
        output.append(getIndent(indent + indent() * 2));
        output.append(isUnambiguousFunctionRef);
        output.append('\n');
        
        name.ifPresent(aName -> output.append(aName.debug(indent + indent())));
        variableRef.ifPresent(aVariableRef -> output.append(aVariableRef.debug(indent + indent())));
        exprList.ifPresent(aExprList -> output.append(aExprList.debug(indent + indent())));
        return output.toString();
    }
    
    public void settleChildren() {
        if (getParent() instanceof AParentChild) {
            refParent = Optional.of(((AParentChild) getParent()).expression);
        }
        IScopeContainer.makeChild(getScope(), this, name);
        IScopeContainer.makeChild(getScope(), this, variableRef);
        IScopeContainer.makeChild(getScope(), this, exprList);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        
        name.ifPresent(aName -> output.addAll(aName.searchAndMerge()));
        variableRef.ifPresent(aVariableRef -> output.addAll(aVariableRef.searchAndMerge()));
        exprList.ifPresent(aFuncParams -> output.addAll(aFuncParams.searchAndMerge()));
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        
        // Ensure that the reference exists
        if (!resolveFunctionReference().isPresent() && !resolveVariableReference().isPresent()) {
            output.add(onNoMatch());
        }
        
        name.ifPresent(aName -> output.addAll(aName.verify()));
        variableRef.ifPresent(aVariableRef -> output.addAll(aVariableRef.verify()));
        exprList.ifPresent(aExprList -> output.addAll(aExprList.verify()));
        return output;
    }
    
    public Optional<AFunctionDec> resolveFunctionReference() {
        // Get name and ensure this could be a function reference
        String n = name.map(aName -> aName.identifier).orElse(null);
        if (n == null) {
            n = variableRef.map(aVariableRef -> aVariableRef.name.identifier).orElse(null);
            // VariableRef must not be a member variable reference for it possibly to be a function
            if (n == null || !variableRef.get().variableType.equals(AVariableRef.AVariableType.GLOBAL)) return Optional.empty();
        }
        
        IScopeContainer scope = getScope();
        while (scope != null) {
            if (scope.getFunctionDecs().isPresent()) {
                for (AFunctionDec functionDec : scope.getFunctionDecs().get()) {
                    if (functionDec.getFullMatches(n, exprList)) {
                        return Optional.of(functionDec);
                    }
                }
            }
            scope = scope.getScope();
        }
        return Optional.empty();
    }
    
    public Optional<AVariable> resolveVariableReference() {
        if (isUnambiguousFunctionRef || !variableRef.isPresent()) return Optional.empty();
        
        IScopeContainer scope = getScope();
        while (scope != null) {
            if (scope.getVariableDecs().isPresent()) {
                for (AVariableDec variableDec : scope.getVariableDecs().get()) {
                    if (variableDec.variableRef.equals(variableRef.get())) {
                        return Optional.of(variableDec);
                    }
                }
            }
            scope = scope.getScope();
        }
        
        ABase parent = getParent();
        while (parent != null) {
            if (parent instanceof AFunctionDec) {
                AArguments arguments = ((AFunctionDec) parent).arguments.orElse(null);
                if (arguments != null) {
                    for (AArgument argument : arguments.arguments) {
                        if (argument.name.equals(variableRef.get().name)) {
                            return Optional.of(argument);
                        }
                    }
                }
            }
            parent = parent.getParent();
        }
        return Optional.empty();
    }
    
    public ATypeDec resolveType() {
        Optional<AFunctionDec> functionDec = resolveFunctionReference();
        if (functionDec.isPresent()) {
            return functionDec.get().typeDec;
        }
        Optional<AVariable> variable = resolveVariableReference();
        return variable.map(AVariable::resolveType).orElse(null);
    }
    
    private BulletError onNoMatch() {
        String n = (name.isPresent() ? name.get().toString() : (variableRef.isPresent() ? variableRef.get().toString() : "?"));
        String msgPt = (isUnambiguousFunctionRef ? "function"
                : ((variableRef.isPresent() && !variableRef.get().variableType.equals(AVariableRef.AVariableType.GLOBAL)
                ? "member variable" : "variable/function")));
        return BulletError.format(ctx, "Invalid %s reference: \"%s\"", msgPt, n);
    }
    
}
