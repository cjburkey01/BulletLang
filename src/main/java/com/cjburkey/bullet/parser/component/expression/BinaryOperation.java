package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.BulletError;
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
public class BinaryOperation extends UnaryOperation {

    private static final String TYPES_DIFFER_ERROR = "Types %s and %s differ in binary operation %s";
    private static final String TYPE_MISSING_ERROR = "%s expression has an invalid type";

    private Expression expressionB;

    private BinaryOperation(ParserRuleContext ctx, Operator operator, Expression expressionA, Expression expressionB) {
        super(ctx, operator, expressionA);
        this.expressionB = expressionB;
    }

    @Override
    public void doResolve(ObjectOpenHashSet<Base> exclude) {
        super.resolve(this, exclude);
        if (!exclude.contains(expressionB)) expressionB.resolve(this, exclude);

        if (expressionA.outputType == null) {
            BulletError.queueError(ctx, TYPE_MISSING_ERROR, "First");
            return;
        }
        if (expressionB.outputType == null) {
            BulletError.queueError(ctx, TYPE_MISSING_ERROR, "Second");
            return;
        }

        // TODO: RESOLVE OPERATORS TO METHODS ON THE LEFT TYPE
        //       FOR NOW, IT WILL JUST ERROR IF THEY'RE DIFFERENT TYPES
        if (!expressionA.outputType.equals(expressionB.outputType)) {
            BulletError.queueError(ctx, TYPES_DIFFER_ERROR, expressionA.outputType, expressionB.outputType, operator);
            return;
        }

        this.outputType = expressionA.outputType;
    }

    @Override
    public String toString() {
        return String.format("Binary expression with operator {%s} between {%s} and {%s}", operator, expressionA, expressionB);
    }

    public static final class Visitor extends BaseV<BinaryOperation> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
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
