package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.InstanceType;
import com.cjburkey.bullet.parser.RawType;
import com.cjburkey.bullet.parser.component.Parameter;
import com.cjburkey.bullet.parser.component.Parameters;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.TypeDec;
import com.cjburkey.bullet.parser.component.classes.ClassInner;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class FunctionDec extends ClassInner implements IScopeContainer {

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
        variableDec.resolveTypes();
        variableDec.resolveReferences();
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
    public void resolveTypes() {
        parameters.resolveTypes();
        if (type != null) type.resolveReferences();
        scope.resolveTypes();

        if (type == null) {
            type = new TypeDec(null, new RawType("Void"));
            type.resolveTypes();
        }
        if (type.type == null) {
            type.type = new RawType("Void");
            type.resolveTypes();
        }
    }

    @Override
    public void resolveReferences() {
        parameters.resolveReferences();
        if (type != null) type.resolveReferences();

        for (Parameter parameter : parameters.parameters) {
            scope.addVariable(createParameterVariable(parameter));
        }

        scope.resolveReferences();
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
