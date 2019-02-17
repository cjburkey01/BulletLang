package com.cjburkey.bullet.parser.component.classes;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class ClassScope extends Scope {

    ClassScope(ParserRuleContext ctx) {
        super(ctx);
    }

    @Override
    public String toString() {
        return statements.toString();
    }

    public static final class Visitor extends BaseV<ClassScope> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<ClassScope> visitClassScope(BulletLangParser.ClassScopeContext ctx) {
            ClassScope scope = visit(ctx.classScope())
                    .orElseGet(() -> new ClassScope(ctx));
            Optional<ClassInner> classInner = new ClassInner.Visitor(scope)
                    .visit(ctx.classInner());
            classInner.ifPresent(scope.statements::add);

            scope.parentScope = this.scope;
            return Optional.of(scope);
        }

    }

}
