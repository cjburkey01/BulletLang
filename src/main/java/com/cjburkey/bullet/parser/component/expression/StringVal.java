package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class StringVal extends Expression {

    public boolean advanced;
    public String value;

    private StringVal(boolean advanced, String value) {
        this.value = value;
    }

    public static final class Visitor extends BaseV<StringVal> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<StringVal> visitStringExpression(BulletLangParser.StringExpressionContext ctx) {
            String raw = ctx.getText();
            boolean smart = raw.startsWith("@");
            return Optional.of(new StringVal(smart, raw.substring(smart ? 2 : 1, raw.length() - 1)));
        }

    }

}
