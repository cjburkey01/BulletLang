package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.Operator;
import com.cjburkey.bullet.parser.RawType;
import com.cjburkey.bullet.parser.component.Scope;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class UnaryOperation extends Expression {

    public Operator operator;
    public Expression expressionA;

    protected UnaryOperation(ParserRuleContext ctx, Operator operator, Expression expressionA) {
        super(ctx);
        this.operator = operator;
        this.expressionA = expressionA;
    }

    public void resolveType() {

    }

    public RawType getType() {
        return null;
    }

    public String toString() {
        return String.format("Unary expression with operator {%s} on {%s}", operator, expressionA);
    }

    public static final class Visitor extends BaseV<UnaryOperation> {

        public Visitor(Scope scope) {
            super(scope);
        }

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
