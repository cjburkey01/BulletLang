package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.IScopeContainer;
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
    private TypeDec typeDec;
    public Scope scope;

    private FunctionDec(ParserRuleContext ctx, String name, Parameters parameters, TypeDec typeDec, Scope scope) {
        super(ctx);
        isConstructor = (name == null);
        this.name = name;
        this.parameters = parameters;
        this.typeDec = typeDec;
        this.scope = scope;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return String.format("Define function {%s} of type {%s} with parameters {%s} and scope {%s}", name, typeDec, parameters, scope);
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
                    .orElseGet(() -> new Scope(null));
            String name = ((ctx.IDENTIFIER() == null) ? null : ctx.IDENTIFIER().getText());
            FunctionDec functionDec = new FunctionDec(ctx, name, parameters, typeDec, scope);
            functionDec.parentScope = this.scope;
            this.scope.addFunction(functionDec);
            return Optional.of(functionDec);
        }

    }

}
