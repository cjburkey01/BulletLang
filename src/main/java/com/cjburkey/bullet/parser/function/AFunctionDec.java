package com.cjburkey.bullet.parser.function;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.AOperator;
import com.cjburkey.bullet.parser.ATypeDec;
import com.cjburkey.bullet.parser.AVariableDec;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.AExprList;
import com.cjburkey.bullet.parser.statement.AScope;
import com.cjburkey.bullet.parser.statement.AStatement;
import com.cjburkey.bullet.parser.statement.AStatementVariableDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class AFunctionDec extends ABase implements IScopeContainer {
    
    public final boolean isConstructor;
    public final Optional<AName> name;
    public final Optional<AOperator> operator;
    public final Optional<AArguments> arguments;
    public final Optional<AScope> scope;
    
    public AFunctionDec(Optional<AName> name, Optional<AOperator> operator, Optional<AArguments> arguments, Optional<AScope> scope,
                        BulletParser.FunctionDecContext ctx) {
        super(ctx);
        
        this.isConstructor = name.map(aName -> aName.identifier.equals("_")).orElse(false);
        this.name = name;
        this.operator = operator;
        this.arguments = arguments;
        this.scope = scope;
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("FunctionDec:\n");
        
        name.ifPresent(aName -> output.append(aName.debug(indent + indent())));
        operator.ifPresent(aOperator -> output.append(aOperator.debug(indent + indent())));
        arguments.ifPresent(aArguments -> output.append(aArguments.debug(indent + indent())));
        scope.ifPresent(aScope -> output.append(aScope.debug(indent + indent())));
        return output.toString();
    }
    
    public void settleChildren() {
        IScopeContainer.makeChild(getScope(), this, name);
        IScopeContainer.makeChild(getScope(), this, arguments);
        IScopeContainer.makeChild(this, this, scope);
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = name.map(AName::verify).orElseGet(ObjectArrayList::new);
        arguments.ifPresent(aArguments -> output.addAll(aArguments.verify()));
        scope.ifPresent(aScope -> output.addAll(aScope.verify()));
        if (!name.isPresent() && !operator.isPresent()) {
            output.add(new BulletError("Invalid function lacking name", ctx));
        }
        return output;
    }
    
    public Optional<Collection<AStatement>> getStatements() {
        return scope.map(aScope -> aScope.statements);
    }
    
    public Optional<Collection<AVariableDec>> getVariableDecs() {
        return scope.map(aScope -> aScope.statements.stream().filter(statement -> (statement instanceof AStatementVariableDec))
                .map(statement -> ((AStatementVariableDec) statement).variableDec).collect(Collectors.toList()));
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        
        final IScopeContainer parentScope = getScope();
        if (parentScope != null && parentScope.getFunctionDecs().isPresent()) {
            for (AFunctionDec functionDec : parentScope.getFunctionDecs().get()) {
                if (functionDec != this && functionDec.name.equals(name) && functionDec.operator.equals(operator)
                        && functionDec.arguments.equals(arguments)) {
                    output.add(onDuplicate(functionDec));
                }
            }
        }
        
        name.ifPresent(aName -> output.addAll(aName.searchAndMerge()));
        arguments.ifPresent(aArguments -> output.addAll(aArguments.searchAndMerge()));
        scope.ifPresent(aScope -> output.addAll(aScope.searchAndMerge()));
        return output;
    }
    
    public boolean getMatches(String name) {
        if (operator.isPresent() && operator.get().token.equals(name)) {
            return true;
        }
        return (this.name.isPresent() && this.name.get().identifier.equals(name));
    }
    
    public boolean getArgumentsMatch(Optional<AExprList> exprList) {
        if (exprList.isPresent() != arguments.isPresent()) return false;
        if (!exprList.isPresent()) return true;
        if (exprList.get().expressions.size() != arguments.get().arguments.size()) return false;
        for (int i = 0; i < exprList.get().expressions.size(); i ++) {
            if (!arguments.get().arguments.get(i).typeDec.equals(Optional.of(exprList.get().expressions.get(i).resolveType()))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean getFullMatches(String name, Optional<AExprList> exprList) {
        return getMatches(name) && getArgumentsMatch(exprList);
    }
    
    private BulletError onDuplicate(AFunctionDec other) {
        ObjectArrayList<String> argTypes = new ObjectArrayList<>();
        if (other.arguments.isPresent()) {
            for (AArgument argument : other.arguments.get().arguments) {
                argTypes.add(argument.typeDec.map(ATypeDec::toString).orElse("?"));
            }
        }
        return BulletError.format(ctx, "Duplicate function: \"%s\" with args of types: %s",
                (this.name.isPresent() ? this.name.get().toString() : (this.operator.isPresent() ? this.operator.get().toString() : "")),
                Arrays.toString(argTypes.toArray(new String[0])));
    }
    
}
