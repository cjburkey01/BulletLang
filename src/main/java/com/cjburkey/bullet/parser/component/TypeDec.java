package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.RawType;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class TypeDec extends Base {

    public RawType type;

    private TypeDec(ParserRuleContext ctx, RawType type) {
        super(ctx);
        this.type = type;
    }

    public String toString() {
        return type.toString();
    }

    public static final class Visitor extends BaseV<TypeDec> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<TypeDec> visitTypeDec(BulletLangParser.TypeDecContext ctx) {
            TypeDec typeDec = new TypeDec(ctx, new RawType(ctx.IDENTIFIER().getText()));
            typeDec.parentScope = scope;
            return Optional.of(typeDec);
        }

    }

}
