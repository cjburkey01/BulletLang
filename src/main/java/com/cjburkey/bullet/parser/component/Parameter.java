package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Objects;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Parameter extends Base {

    public String name;
    public TypeDec type;

    private Parameter(ParserRuleContext ctx, String name, TypeDec type) {
        super(ctx);
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Parameter {%s}%s", name, ((type == null) ? "" : (" of type {" + type + '}')));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(name, parameter.name) &&
                Objects.equals(type, parameter.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public void doResolve(ObjectOpenHashSet<Base> exclude) {
        if (!exclude.contains(type)) type.resolve(this, exclude);
    }

    public static final class Visitor extends BaseV<Parameter> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<Parameter> visitParameter(BulletLangParser.ParameterContext ctx) {
            TypeDec typeDec = new TypeDec.Visitor(scope)
                    .visit(ctx.typeDec())
                    .orElse(null);
            Parameter parameter = new Parameter(ctx, ctx.IDENTIFIER().getText(), typeDec);
            parameter.parentScope = scope;
            return Optional.of(parameter);
        }

    }

}
