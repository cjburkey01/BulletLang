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
public class BooleanVal extends Expression {

    public boolean value;

    private BooleanVal(ParserRuleContext ctx, boolean value) {
        super(ctx);
        this.value = value;
    }

    public void resolveType() {
    }

    public RawType getType() {
        return new RawType("Boolean");
    }

    public String toString() {
        return "Boolean: {" + value + "}";
    }

    public static final class Visitor extends BaseV<BooleanVal> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<BooleanVal> visitBooleanExpression(BulletLangParser.BooleanExpressionContext ctx) {
            try {
                BooleanVal booleanVal = new BooleanVal(ctx, ctx.TRUE() != null);
                booleanVal.parentScope = scope;
                return Optional.of(booleanVal);
            } catch (Exception ignored) {
            }
            return Optional.empty();
        }

    }

}
