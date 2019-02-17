package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.expression.Expression;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class ReturnVal extends Statement {

    public Expression value;

    private ReturnVal(Expression value) {
        this.value = value;
    }

    public void execute() {

    }

    public static final class Visitor extends BaseV<ReturnVal> {

        public Visitor(Scope parentScope) {
            super(parentScope);
        }

        public Optional<ReturnVal> visitReturnVal(BulletLangParser.ReturnValContext ctx) {
            return Optional.of(new ReturnVal(new Expression.Visitor(scope)
                    .visit(ctx.expression())
                    .orElse(null)));
        }

    }

}
