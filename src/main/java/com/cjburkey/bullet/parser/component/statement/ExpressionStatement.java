package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.expression.Expression;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class ExpressionStatement extends Statement {

    private Expression value;

    private ExpressionStatement(ParserRuleContext ctx, Expression value) {
        super(ctx);
        this.value = value;
    }

    @Override
    public String toString() {
        return "Expression: {" + value + "}";
    }

    @Override
    public void doResolve(ObjectOpenHashSet<Base> exclude) {
        if (!exclude.contains(value)) value.resolve(this, exclude);
    }

    public static final class Visitor extends BaseV<ExpressionStatement> {

        public Visitor(Scope parentScope) {
            super(parentScope);
        }

        @Override
        public Optional<ExpressionStatement> visitExpressionStatement(BulletLangParser.ExpressionStatementContext ctx) {
            Expression value = new Expression.Visitor(scope)
                    .visit(ctx.expression())
                    .orElse(null);
            ExpressionStatement expressionStatement = new ExpressionStatement(ctx, value);
            expressionStatement.parentScope = scope;
            return Optional.of(expressionStatement);
        }

    }

}
