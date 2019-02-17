package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.expression.Expression;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class ExpressionStatement extends Statement {

    public Expression value;

    private ExpressionStatement(Expression value) {
        this.value = value;
    }

    public void execute() {

    }

    public static final class Visitor extends BaseV<ExpressionStatement> {

        public Visitor(Scope parentScope) {
            super(parentScope);
        }

        public Optional<ExpressionStatement> visitExpressionStatement(BulletLangParser.ExpressionStatementContext ctx) {
            Expression value = new Expression.Visitor(scope)
                    .visit(ctx.expression())
                    .orElse(null);
            ExpressionStatement expressionStatement = new ExpressionStatement(value);
            expressionStatement.parentScope = scope;
            return Optional.of(expressionStatement);
        }

    }

}
