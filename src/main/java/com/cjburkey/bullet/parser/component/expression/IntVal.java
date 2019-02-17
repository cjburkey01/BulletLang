package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.RawType;
import com.cjburkey.bullet.parser.component.Scope;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class IntVal extends Expression {

    private long value;

    private IntVal(ParserRuleContext ctx, long value) {
        super(ctx);
        this.value = value;
    }

    @Override
    public void resolveType() {
    }

    @Override
    public RawType getType() {
        return new RawType("Int64");
    }

    @Override
    public String toString() {
        return "Int: {" + value + "}";
    }

    public static final class Visitor extends BaseV<IntVal> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<IntVal> visitIntegerExpression(BulletLangParser.IntegerExpressionContext ctx) {
            try {
                IntVal intVal = new IntVal(ctx, Long.parseLong(ctx.INTEGER().getText()));
                intVal.parentScope = scope;
                return Optional.of(intVal);
            } catch (Exception ignored) {
            }
            return Optional.empty();
        }

    }

}
