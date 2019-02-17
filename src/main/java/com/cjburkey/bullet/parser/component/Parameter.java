package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Parameter extends Base {

    public String name;
    public TypeDec type;

    private Parameter(String name, TypeDec type) {
        this.name = name;
        this.type = type;
    }

    public static final class Visitor extends BaseV<Parameter> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<Parameter> visitParameter(BulletLangParser.ParameterContext ctx) {
            TypeDec typeDec = new TypeDec.Visitor(scope)
                    .visit(ctx.typeDec())
                    .orElse(null);
            Parameter parameter = new Parameter(ctx.IDENTIFIER().getText(), typeDec);
            parameter.parentScope = scope;
            return Optional.of(parameter);
        }

    }

}
