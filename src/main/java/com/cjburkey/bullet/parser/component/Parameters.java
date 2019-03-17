package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Objects;
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

    @Override
    public void doResolve(ObjectOpenHashSet<Base> exclude) {
        parameters.forEach(parameter -> {
            if (!exclude.contains(parameter)) parameter.resolve(this, exclude);
        });
    }

    @Override
    public String toString() {
        return parameters.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameters that = (Parameters) o;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters);
    }

    public static final class Visitor extends BaseV<Parameters> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
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
