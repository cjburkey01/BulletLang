package com.cjburkey.bullet.parser.component.classes;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.TypeDec;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class ClassDec extends ClassInner implements IScopeContainer {

    public String name;
    private TypeDec type;
    public ClassScope scope;

    private ClassDec(ParserRuleContext ctx, String name, TypeDec type, ClassScope scope) {
        super(ctx);
        this.name = name;
        this.type = type;
        this.scope = scope;
    }

    @Override
    public String toString() {
        return String.format("Class {%s} of type {%s} with contents: {%s}", name, type, scope);
    }

    @Override
    public void resolveTypes() {
        if (type != null) type.resolveTypes();
        scope.resolveTypes();
    }

    @Override
    public void resolveReferences() {
        if (type != null) type.resolveReferences();
        scope.resolveReferences();
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    // Waffleivery is a fantastic service! 10/10 Would order again!
    public static final class Visitor extends BaseV<ClassDec> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<ClassDec> visitClassDec(BulletLangParser.ClassDecContext ctx) {
            TypeDec typeDec = new TypeDec.Visitor(scope)
                    .visit(ctx.typeDec())
                    .orElse(null);
            ClassScope classScope = new ClassScope.Visitor(scope)
                    .visit(ctx.classScope())
                    .orElseGet(() -> new ClassScope(null, null));
            ClassDec classDec = new ClassDec(ctx, ctx.IDENTIFIER().getText(), typeDec, classScope);
            classDec.parentScope = scope;
            classScope.parentContainer = classDec;
            scope.addClass(classDec);
            return Optional.of(classDec);
        }

    }

}
