package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.BulletCompiler;
import com.cjburkey.bullet.Log;
import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.RawType;
import com.cjburkey.bullet.parser.component.Scope;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class StringVal extends Expression {

    public String[] string;
    private Expression[] expressions;

    private StringVal(ParserRuleContext ctx, String[] string, Expression[] expressions) {
        super(ctx);
        this.string = string;
        this.expressions = expressions;
    }

    @Override
    public void resolve(ObjectOpenHashSet<Base> exclude) {
        outputType = new RawType("String");
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        if (expressions.length > 0) {
            output.append("Smart string");
        } else {
            output.append('S');
        }
        output.append("String: {\"");

        for (int i = 0; i < string.length || i < expressions.length; i++) {
            if (i < string.length) output.append(string[i]
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t"));
            if (i < expressions.length) {
                output.append('{');
                output.append(expressions[i]);
                output.append('}');
            }
        }

        return output.append("\"}").toString();
    }

    public static final class Visitor extends BaseV<StringVal> {

        public Visitor(Scope scope) {
            super(scope);
        }

        // Zoo-wee this'n's a tough'n
        @Override
        public Optional<StringVal> visitStringExpression(BulletLangParser.StringExpressionContext ctx) {
            String raw = ctx.getText();
            boolean smart = raw.startsWith("@");
            raw = raw.substring((smart ? 2 : 1), raw.length() - 1);

            String[] stringOut;
            Expression[] expressions;
            if (smart) {
                // Separate expressions
                ObjectArrayList<String> string = new ObjectArrayList<>();
                ObjectArrayList<String> rawExpressions = new ObjectArrayList<>();
                int last = 0;
                int start = 0;
                int depth = 0;
                for (int i = 0; i < raw.length(); i++) {
                    char at = raw.charAt(i);
                    if (at == '{') {
                        if (depth == 0) {
                            start = i;
                        }

                        depth++;
                    } else if (at == '}') {
                        depth--;

                        if (depth == 0) {
                            rawExpressions.add(raw.substring(start + 1, i));
                            string.add(raw.substring(last, start));
                            last = i + 1;
                        }
                    }
                }
                if (last < raw.length()) {
                    string.add(raw.substring(last));
                }

                // Parse expressions
                expressions = new Expression[rawExpressions.size()];
                int i = 0;
                for (String rawExpression : rawExpressions) {
                    try {
                        BulletLangParser parser = BulletCompiler.createParser(rawExpression);
                        expressions[i] = new Expression.Visitor(scope)
                                .visit(parser.rawExpr().expression())
                                .orElse(null);
                    } catch (Exception e) {
                        Log.exception(e);
                    }

                    i++;
                }

                stringOut = string.toArray(new String[0]);
            } else {
                stringOut = new String[] {raw};
                expressions = new Expression[0];
            }

            StringVal stringVal = new StringVal(ctx, stringOut, expressions);
            stringVal.parentScope = scope;
            return Optional.of(stringVal);
        }

    }

}
