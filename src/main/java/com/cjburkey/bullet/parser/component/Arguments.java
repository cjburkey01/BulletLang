package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.expression.Expression;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Arguments extends Base {

    private final ObjectArrayList<Expression> arguments = new ObjectArrayList<>();

    private Arguments(ParserRuleContext ctx) {
        super(ctx);
    }

    @Override
    public String toString() {
        return arguments.toString();
    }

    public static final class Visitor extends BaseV<Arguments> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<Arguments> visitArguments(BulletLangParser.ArgumentsContext ctx) {
            Arguments arguments = visit(ctx.arguments()).orElseGet(() -> new Arguments(ctx));
            Optional<Expression> argument = new Expression.Visitor(scope)
                    .visit(ctx.expression());
            argument.ifPresent(arguments.arguments::add);
            arguments.parentScope = scope;
            return Optional.of(arguments);
        }

    }

}
