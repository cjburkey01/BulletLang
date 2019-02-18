package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.RawType;
import java.util.Objects;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class TypeDec extends Base {

    public RawType type;

    public TypeDec(ParserRuleContext ctx, RawType type) {
        super(ctx);
        this.type = type;
    }

    @Override
    public String toString() {
        return type + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeDec typeDec = (TypeDec) o;
        return Objects.equals(type, typeDec.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public void resolveTypes() {
        if (type != null) type.resolveTypes();
    }

    @Override
    public void resolveReferences() {
        if (type != null) type.resolveReferences();
    }

    public static final class Visitor extends BaseV<TypeDec> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<TypeDec> visitTypeDec(BulletLangParser.TypeDecContext ctx) {
            TypeDec typeDec = new TypeDec(ctx, new RawType(ctx.IDENTIFIER().getText()));
            typeDec.parentScope = scope;
            return Optional.of(typeDec);
        }

    }

}
