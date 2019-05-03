package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.*;
import com.cjburkey.bullet.parser.component.Parameter;
import com.cjburkey.bullet.parser.component.Parameters;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.TypeDec;
import com.cjburkey.bullet.parser.component.classes.ClassInner;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class FunctionDec extends ClassInner implements IScopeContainer {

    @SuppressWarnings("FieldCanBeLocal")
    private boolean isConstructor;
    public String name;
    public Parameters parameters;
    public TypeDec type;
    public Scope scope;

    private FunctionDec(ParserRuleContext ctx, String name, Parameters parameters, TypeDec type, Scope scope) {
        super(ctx);
        isConstructor = (name == null);
        this.name = name;
        this.parameters = parameters;
        this.type = type;
        this.scope = scope;
    }

    private VariableDec createParameterVariable(Parameter parameter) {
        VariableDec variableDec = new VariableDec(parameter.ctx, InstanceType.DEFAULT, parameter.name, parameter.type, null);
        variableDec.resolve(this, new ObjectOpenHashSet<>());
        return variableDec;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return String.format("Define function {%s} of type {%s} with parameters {%s} and scope {%s}", name, type, parameters, scope);
    }

    @Override
    public void doResolve(ObjectOpenHashSet<Base> exclude) {
        if (!exclude.contains(parameters)) parameters.resolve(this, exclude);
        if (type != null && !exclude.contains(type)) type.resolve(this, exclude);

        // Parameters must be resolved before scope so that the function scope may access them as variables
        for (Parameter parameter : parameters.parameters) {
            scope.addVariable(createParameterVariable(parameter));
        }

        if (!exclude.contains(scope)) scope.resolve(this, exclude);

        if (type == null) {
            type = TypeDec.of(RawType.BasicTypes.VOID);
            type.resolve(this, exclude);
        }
        if (type.type == null) {
            type.type = RawType.BasicTypes.VOID;
            type.resolve(this, exclude);
        }

    }

    public static final class Visitor extends BaseV<FunctionDec> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<FunctionDec> visitFunctionDec(BulletLangParser.FunctionDecContext ctx) {
            Parameters parameters = new Parameters.Visitor(this.scope)
                    .visit(ctx.parameters())
                    .orElseGet(() -> new Parameters(null));
            TypeDec typeDec = new TypeDec.Visitor(this.scope)
                    .visit(ctx.typeDec())
                    .orElse(null);
            Scope scope = new Scope.Visitor(this.scope)
                    .visit(ctx.scope())
                    .orElseGet(() -> new Scope(null, null));
            String name = ((ctx.IDENTIFIER() == null) ? null : ctx.IDENTIFIER().getText());
            FunctionDec functionDec = new FunctionDec(ctx, name, parameters, typeDec, scope);
            functionDec.parentScope = this.scope;
            scope.parentContainer = functionDec;
            this.scope.addFunction(functionDec);
            return Optional.of(functionDec);
        }

    }

}
