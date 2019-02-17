package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.ITyped;
import com.cjburkey.bullet.parser.component.Scope;
import java.util.Optional;
import java.util.function.Function;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public abstract class Expression extends Base implements ITyped {

    public Expression(ParserRuleContext ctx) {
        super(ctx);
    }

    public static final class Visitor extends BaseV<Expression> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<Expression> visitFloatExpression(BulletLangParser.FloatExpressionContext ctx) {
            return new FloatVal.Visitor(scope)
                    .visit(ctx)
                    .map(Function.identity());
        }

        public Optional<Expression> visitIntegerExpression(BulletLangParser.IntegerExpressionContext ctx) {
            return new IntVal.Visitor(scope)
                    .visit(ctx)
                    .map(Function.identity());
        }

        public Optional<Expression> visitStringExpression(BulletLangParser.StringExpressionContext ctx) {
            return new StringVal.Visitor(scope)
                    .visit(ctx)
                    .map(Function.identity());
        }

        public Optional<Expression> visitUnOpExpression(BulletLangParser.UnOpExpressionContext ctx) {
            return new UnaryOperation.Visitor(scope)
                    .visit(ctx)
                    .map(Function.identity());
        }

        public Optional<Expression> visitBinOpExpression(BulletLangParser.BinOpExpressionContext ctx) {
            return new BinaryOperation.Visitor(scope)
                    .visit(ctx)
                    .map(Function.identity());
        }

        public Optional<Expression> visitBooleanExpression(BulletLangParser.BooleanExpressionContext ctx) {
            return new BooleanVal.Visitor(scope)
                    .visit(ctx)
                    .map(Function.identity());
        }

        public Optional<Expression> visitReferenceExpression(BulletLangParser.ReferenceExpressionContext ctx) {
            return new Reference.Visitor(scope)
                    .visit(ctx.reference())
                    .map(Function.identity());
        }

    }

}
