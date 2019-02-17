package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.TypeDec;
import com.cjburkey.bullet.parser.component.classes.ClassInner;
import com.cjburkey.bullet.parser.component.expression.Expression;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class VariableDec extends ClassInner {

    public String name;
    private TypeDec type;
    private Expression value;

    private VariableDec(ParserRuleContext ctx, String name, TypeDec type, Expression value) {
        super(ctx);
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Declare variable {%s} of type {%s} with value {%s}", name, type, value);
    }

    public static final class Visitor extends BaseV<VariableDec> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<VariableDec> visitVariableDec(BulletLangParser.VariableDecContext ctx) {
            TypeDec typeDec = new TypeDec.Visitor(scope)
                    .visit(ctx.typeDec())
                    .orElse(null);
            Expression expression = new Expression.Visitor(scope)
                    .visit(ctx.expression())
                    .orElse(null);
            VariableDec variableDec = new VariableDec(ctx, ctx.IDENTIFIER().getText(), typeDec, expression);
            variableDec.parentScope = scope;
            scope.addVariable(variableDec);
            return Optional.of(variableDec);
        }

    }

}
