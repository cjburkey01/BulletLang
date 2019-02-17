package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.TypeDec;
import com.cjburkey.bullet.parser.component.expression.Expression;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class VariableDec extends Statement {

    public String name;
    public TypeDec type;
    public Expression value;

    private VariableDec(String name, TypeDec type, Expression value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public void execute() {

    }

    public static final class Visitor extends BaseV<VariableDec> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<VariableDec> visitVariableDec(BulletLangParser.VariableDecContext ctx) {
            TypeDec typeDec = new TypeDec.Visitor(scope)
                    .visit(ctx.typeDec())
                    .orElse(null);
            Expression expression = new Expression.Visitor(scope)
                    .visit(ctx.expression())
                    .orElse(null);
            VariableDec variableDec = new VariableDec(ctx.IDENTIFIER().getText(), typeDec, expression);
            variableDec.parentScope = scope;
            scope.addVariable(variableDec);
            return Optional.of(variableDec);
        }

    }

}
