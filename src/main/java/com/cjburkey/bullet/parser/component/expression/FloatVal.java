package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class FloatVal extends Expression {

    public double value;

    private FloatVal(double value) {
        this.value = value;
    }

    public static final class Visitor extends BaseV<FloatVal> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<FloatVal> visitFloatExpression(BulletLangParser.FloatExpressionContext ctx) {
            try {
                FloatVal floatVal = new FloatVal(Double.parseDouble(ctx.FLOAT().getText()));
                floatVal.parentScope = scope;
                return Optional.of(floatVal);
            } catch (Exception ignored) {
            }
            return Optional.empty();
        }

    }

}
