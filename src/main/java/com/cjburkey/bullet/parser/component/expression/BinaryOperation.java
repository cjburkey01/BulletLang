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
public class BinaryOperation extends UnaryOperation {

    public Expression expressionB;

    private BinaryOperation(ParserRuleContext ctx, Operator operator, Expression expressionA, Expression expressionB) {
        super(ctx, operator, expressionA);
        this.expressionB = expressionB;
    }

    public void resolveType() {

    }

    public RawType getType() {
        return null;
    }

    public String toString() {
        return String.format("Binary expression with operator {%s} between {%s} and {%s}", operator, expressionA, expressionB);
    }

    public static final class Visitor extends BaseV<BinaryOperation> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<BinaryOperation> visitBinOpExpression(BulletLangParser.BinOpExpressionContext ctx) {
            Operator operator = Operator.resolve(ctx.op.getText().trim());
            Expression expressionA = new Expression.Visitor(scope)
                    .visit(ctx.expression(0))
                    .orElse(null);
            Expression expressionB = new Expression.Visitor(scope)
                    .visit(ctx.expression(1))
                    .orElse(null);
            BinaryOperation binaryOperation = new BinaryOperation(ctx, operator, expressionA, expressionB);
            binaryOperation.parentScope = scope;
            return Optional.of(binaryOperation);
        }

    }

}
