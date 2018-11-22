package com.cjburkey.bullet.parser.function;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.AOperator;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.statement.AScope;
import com.cjburkey.bullet.parser.statement.AStatement;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class AFunctionDec extends ABase implements IScopeContainer {
    
    public Optional<AName> name;
    public Optional<AOperator> operator;
    public Optional<AArguments> arguments;
    public Optional<AScope> scope;
    
    public AFunctionDec(Optional<AName> name, Optional<AOperator> operator, Optional<AArguments> arguments, Optional<AScope> scope,
                        BulletParser.FunctionDecContext ctx) {
        super(ctx);
        
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
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        
        final IScopeContainer parentScope = getScope();
        if (parentScope != null && parentScope.getFunctionDecs().isPresent()) {
            for (AFunctionDec functionDec : parentScope.getFunctionDecs().get()) {
                if (functionDec != this && functionDec.name.equals(name) && functionDec.operator.equals(operator)
                        && functionDec.arguments.equals(arguments)) {
                    //noinspection OptionalGetWithoutIsPresent
                    output.add(new BulletError("Duplicate function: " +
                            (name.isPresent() ? name.get() : operator.get()), ctx));    // TODO: SHOW ARGS (I'M IN A RUSH)
                }
            }
        }
        
        name.ifPresent(aName -> output.addAll(aName.searchAndMerge()));
        arguments.ifPresent(aArguments -> output.addAll(aArguments.searchAndMerge()));
        scope.ifPresent(aScope -> output.addAll(aScope.searchAndMerge()));
        return output;
    }
    
}
