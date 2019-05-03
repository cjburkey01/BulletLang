package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.classes.ClassDec;
import java.util.Optional;
import java.util.function.Function;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public abstract class Statement extends Base {

    public Statement(ParserRuleContext ctx) {
        super(ctx);
    }

    public static final class Visitor extends BaseV<Statement> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<Statement> visitReturnStatement(BulletLangParser.ReturnStatementContext ctx) {
            return new ReturnVal.Visitor(scope)
                    .visit(ctx.returnVal())
                    .map(Function.identity());
        }

        @Override
        public Optional<Statement> visitExpressionStatement(BulletLangParser.ExpressionStatementContext ctx) {
            return new ExpressionStatement.Visitor(scope)
                    .visit(ctx)
                    .map(Function.identity());
        }

        @Override
        public Optional<Statement> visitVariableDecStatement(BulletLangParser.VariableDecStatementContext ctx) {
            return new VariableDec.Visitor(scope)
                    .visit(ctx.variableDec())
                    .map(Function.identity());
        }

        @Override
        public Optional<Statement> visitFunctionDecStatement(BulletLangParser.FunctionDecStatementContext ctx) {
            return new FunctionDec.Visitor(scope)
                    .visit(ctx.functionDec())
                    .map(Function.identity());
        }

        @Override
        public Optional<Statement> visitClassDecStatement(BulletLangParser.ClassDecStatementContext ctx) {
            return new ClassDec.Visitor(scope)
                    .visit(ctx.classDec())
                    .map(Function.identity());
        }

        @Override
        public Optional<Statement> visitIfStatement(BulletLangParser.IfStatementContext ctx) {
            return new IfStatement.Visitor(scope)
                    .visit(ctx)
                    .map(Function.identity());
        }

    }

}
