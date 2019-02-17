package com.cjburkey.bullet.parser.component.classes;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.statement.FunctionDec;
import com.cjburkey.bullet.parser.component.statement.Statement;
import com.cjburkey.bullet.parser.component.statement.VariableDec;
import java.util.Optional;
import java.util.function.Function;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/17
 */
public abstract class ClassInner extends Statement {

    public ClassInner(ParserRuleContext ctx) {
        super(ctx);
    }

    public static final class Visitor extends BaseV<ClassInner> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<ClassInner> visitVariableDecClass(BulletLangParser.VariableDecClassContext ctx) {
            return new VariableDec.Visitor(scope)
                    .visit(ctx.variableDec())
                    .map(Function.identity());
        }

        @Override
        public Optional<ClassInner> visitFunctionDecClass(BulletLangParser.FunctionDecClassContext ctx) {
            return new FunctionDec.Visitor(scope)
                    .visit(ctx.functionDec())
                    .map(Function.identity());
        }

        @Override
        public Optional<ClassInner> visitClassDecClass(BulletLangParser.ClassDecClassContext ctx) {
            return new ClassDec.Visitor(scope)
                    .visit(ctx.classDec())
                    .map(Function.identity());
        }

    }

}
