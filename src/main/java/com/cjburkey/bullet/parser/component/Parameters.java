package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Parameters extends Base {

    public final ObjectArrayList<Parameter> parameters = new ObjectArrayList<>();

    public Parameters(ParserRuleContext ctx) {
        super(ctx);
    }

    public String toString() {
        return parameters.toString();
    }

    public static final class Visitor extends BaseV<Parameters> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<Parameters> visitParameters(BulletLangParser.ParametersContext ctx) {
            Parameters parameters = visit(ctx.parameters()).orElseGet(() -> new Parameters(ctx));
            Optional<Parameter> parameter = new Parameter.Visitor(scope)
                    .visit(ctx.parameter());
            parameter.ifPresent(parameters.parameters::add);
            parameters.parentScope = scope;
            return Optional.of(parameters);
        }

    }

}
