package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.RawType;
import com.cjburkey.bullet.parser.component.Arguments;
import com.cjburkey.bullet.parser.component.Scope;
import java.util.Objects;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Reference extends Expression {

    public String name;
    public Arguments arguments;
    public boolean ambiguous;

    private Reference(ParserRuleContext ctx, String name, Arguments arguments) {
        super(ctx);
        this.name = name;
        this.arguments = arguments;
        this.ambiguous = false;
    }

    private Reference(ParserRuleContext ctx, String name, boolean ambiguous) {
        super(ctx);
        this.name = name;
        this.ambiguous = ambiguous;
    }

    public void resolveType() {
    }

    public RawType getType() {
        return null;
    }

    public String toString() {
        if (ambiguous) {
            return String.format("Variable/Function reference to {%s}", name);
        }
        return String.format("Function reference to {%s} with arguments %s", name, arguments);
    }

    public static final class Visitor extends BaseV<Reference> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<Reference> visitReference(BulletLangParser.ReferenceContext ctx) {
            Arguments arguments = new Arguments.Visitor(scope)
                    .visit(ctx.arguments())
                    .orElse(null);
            String name = ctx.IDENTIFIER().getText();
            Reference reference = ((arguments == null)
                    ? (new Reference(ctx, name, Objects.isNull(ctx.LEFT_PAR())))
                    : (new Reference(ctx, name, arguments)));
            reference.parentScope = scope;
            return Optional.of(reference);
        }

    }

}
