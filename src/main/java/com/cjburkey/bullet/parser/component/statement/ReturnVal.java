package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.expression.Expression;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class ReturnVal extends Statement {

    public Expression value;

    private ReturnVal(ParserRuleContext ctx, Expression value) {
        super(ctx);
        this.value = value;
    }

    public void execute() {

    }

    public String toString() {
        return "Return: {" + value + "}";
    }

    public static final class Visitor extends BaseV<ReturnVal> {

        public Visitor(Scope parentScope) {
            super(parentScope);
        }

        public Optional<ReturnVal> visitReturnVal(BulletLangParser.ReturnValContext ctx) {
            return Optional.of(new ReturnVal(ctx, new Expression.Visitor(scope)
                    .visit(ctx.expression())
                    .orElse(null)));
        }

    }

}
