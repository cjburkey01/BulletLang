package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class IntVal extends Expression {

    public long value;

    private IntVal(long value) {
        this.value = value;
    }

    public static final class Visitor extends BaseV<IntVal> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<IntVal> visitIntegerExpression(BulletLangParser.IntegerExpressionContext ctx) {
            try {
                IntVal intVal = new IntVal(Long.parseLong(ctx.INTEGER().getText()));
                intVal.parentScope = scope;
                return Optional.of(intVal);
            } catch (Exception ignored) {
            }
            return Optional.empty();
        }

    }

}
