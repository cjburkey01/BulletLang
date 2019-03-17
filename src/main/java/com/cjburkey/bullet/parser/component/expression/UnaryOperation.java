package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.Operator;
import com.cjburkey.bullet.parser.component.Scope;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class UnaryOperation extends Expression {

    Operator operator;
    Expression expressionA;

    UnaryOperation(ParserRuleContext ctx, Operator operator, Expression expressionA) {
        super(ctx);
        this.operator = operator;
        this.expressionA = expressionA;
    }

    @Override
    public String toString() {
        return String.format("Unary expression with operator {%s} on {%s}", operator, expressionA);
    }

    @Override
    public void resolve(ObjectOpenHashSet<Base> exclude) {
        if (!exclude.contains(expressionA)) expressionA.resolve(exclude);
        outputType = expressionA.outputType;
    }

    public static final class Visitor extends BaseV<UnaryOperation> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<UnaryOperation> visitUnOpExpression(BulletLangParser.UnOpExpressionContext ctx) {
            Operator operator = Operator.resolve(ctx.op.getText().trim());
            Expression expressionA = new Expression.Visitor(scope)
                    .visit(ctx.expression())
                    .orElse(null);
            UnaryOperation unaryOperation = new UnaryOperation(ctx, operator, expressionA);
            unaryOperation.parentScope = scope;
            return Optional.of(unaryOperation);
        }

    }

}
