package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class TypeDec extends Base {

    public String type;

    private TypeDec(String type) {
        this.type = type;
    }

    public static final class Visitor extends BaseV<TypeDec> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<TypeDec> visitTypeDec(BulletLangParser.TypeDecContext ctx) {
            TypeDec typeDec = new TypeDec(ctx.IDENTIFIER().getText());
            typeDec.parentScope = scope;
            return Optional.of(typeDec);
        }

    }

}
