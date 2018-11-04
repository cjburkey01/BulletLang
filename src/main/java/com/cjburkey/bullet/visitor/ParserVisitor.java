package com.cjburkey.bullet.visitor;

import com.cjburkey.bullet.antlr.BulletBaseVisitor;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.BFunction;
import com.cjburkey.bullet.obj.BProgram;
import com.cjburkey.bullet.obj.statement.BArgument;
import com.cjburkey.bullet.obj.statement.BExpressionStatement;
import com.cjburkey.bullet.obj.statement.BIfStatement;
import com.cjburkey.bullet.obj.statement.BStatement;
import com.cjburkey.bullet.obj.statement.BVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class ParserVisitor {
    
    private static final ProgramVisitor programVisitor = new ProgramVisitor();
    private static final ProgramInVisitor programInVisitor = new ProgramInVisitor();
    private static final FunctionVisitor functionVisitor = new FunctionVisitor();
    private static final ArgumentsVisitor argumentsVisitor = new ArgumentsVisitor();
    private static final ArgumentVisitor argumentVisitor = new ArgumentVisitor();
    private static final StatementsVisitor statementsVisitor = new StatementsVisitor();
    private static final StatementVisitor statementVisitor = new StatementVisitor();
    private static final VariableDefVisitor variableDefVisitor = new VariableDefVisitor();
    private static final IfStatementVisitor ifStatementVisitor = new IfStatementVisitor();
    private static final ExpressionVisitor expressionVisitor = new ExpressionVisitor();
    private static final FuncParamsVisitor funcParamsVisitor = new FuncParamsVisitor();
    
    public static BProgram parse(BulletParser parser) {
        return programVisitor.visitProgram(parser.program());
    }
    
    private static class ProgramVisitor extends BulletBaseVisitor<BProgram> {
        public BProgram visitProgram(BulletParser.ProgramContext ctx) {
            String namespace = (ctx.namespace() != null && ctx.namespace().IDENTIFIER() != null) ? ctx.namespace().IDENTIFIER().getText() : "";
            ProgramIn programContents = programInVisitor.visitProgramIn(ctx.programIn());
            return new BProgram(namespace, programContents.functions, programContents.statements, ctx);
        }
    }
    
    private static class ProgramInVisitor extends BulletBaseVisitor<ProgramIn> {
        public ProgramIn visitProgramIn(BulletParser.ProgramInContext ctx) {
            if (ctx == null) {
                return new ProgramIn();
            }
            ProgramIn programIn = visitProgramIn(ctx.programIn());
            if (ctx.function() != null) {
                BFunction function = functionVisitor.visitFunction(ctx.function());
                if (function != null) {
                    programIn.functions.add(0, function);
                }
            } else if (ctx.statement() != null) {
                BStatement statement = statementVisitor.visit(ctx.statement());
                if (statement != null) {
                    programIn.statements.add(0, statement);
                }
            }
            return programIn;
        }
    }
    
    private static class FunctionVisitor extends BulletBaseVisitor<BFunction> {
        public BFunction visitFunction(BulletParser.FunctionContext ctx) {
            if (ctx == null) {
                return null;
            }
            String name = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : null;
            String type = (ctx.functionType() != null && ctx.functionType().IDENTIFIER() != null) ? ctx.functionType().IDENTIFIER().getText() : null;
            List<BArgument> arguments = argumentsVisitor.visitArguments(ctx.arguments());
            List<BStatement> statements = statementsVisitor.visitStatements(ctx.statements());
            return new BFunction(name, type, arguments, statements, ctx);
        }
    }
    
    private static class ArgumentsVisitor extends BulletBaseVisitor<List<BArgument>> {
        public List<BArgument> visitArguments(BulletParser.ArgumentsContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<BArgument> arguments = visitArguments(ctx.arguments());
            BArgument argument = argumentVisitor.visitArgument(ctx.argument());
            if (argument != null) {
                arguments.add(0, argument);
            }
            return arguments;
        }
    }
    
    private static class ArgumentVisitor extends BulletBaseVisitor<BArgument> {
        public BArgument visitArgument(BulletParser.ArgumentContext ctx) {
            if (ctx == null) {
                return null;
            }
            String name = ctx.IDENTIFIER(0).getText();
            String type = ctx.IDENTIFIER().size() > 1 ? ctx.IDENTIFIER(1).getText() : null;
            return new BArgument(name, type, ctx);
        }
    }
    
    private static class StatementsVisitor extends BulletBaseVisitor<List<BStatement>> {
        public List<BStatement> visitStatements(BulletParser.StatementsContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<BStatement> statements = visitStatements(ctx.statements());
            if (ctx.statement() != null) {
                BStatement statement = statementVisitor.visit(ctx.statement());
                if (statement != null) {
                    statements.add(0, statement);
                }
            }
            return statements;
        }
    }
    
    private static class StatementVisitor extends BulletBaseVisitor<BStatement> {
        public BStatement visitStatementVariableDef(BulletParser.StatementVariableDefContext ctx) {
            return variableDefVisitor.visitVariableDef(ctx.variableDef());
        }
        public BStatement visitStatementIf(BulletParser.StatementIfContext ctx) {
            return ifStatementVisitor.visitIfStatement(ctx.ifStatement());
        }
        public BStatement visitStatementExpression(BulletParser.StatementExpressionContext ctx) {
            BExpression expression = expressionVisitor.visit(ctx.expression());
            if (expression != null) {
                return new BExpressionStatement(expression, ctx.expression());
            }
            return null;
        }
    }
    
    private static class VariableDefVisitor extends BulletBaseVisitor<BVariable> {
        public BVariable visitVariableDef(BulletParser.VariableDefContext ctx) {
            if (ctx == null) {
                return null;
            }
            String name = ctx.IDENTIFIER(0).getText();
            String type = ctx.IDENTIFIER().size() > 1 ? ctx.IDENTIFIER(1).getText() : null;
            BExpression value = (ctx.variableVal() != null && ctx.variableVal().expression() != null) ? expressionVisitor.visit(ctx.variableVal().expression()) : null;
            return new BVariable(name, type, value, ctx);
        }
    }
    
    private static class IfStatementVisitor extends BulletBaseVisitor<BIfStatement> {
        public BIfStatement visitIfStatement(BulletParser.IfStatementContext ctx) {
            if (ctx == null) {
                return null;
            }
            if (ctx.expression() != null) {
                BExpression condition = expressionVisitor.visit(ctx.expression());
                List<BStatement> statements = ctx.statements() == null ? new ArrayList<>() : statementsVisitor.visitStatements(ctx.statements());
                return new BIfStatement(ctx.ELSE() != null, condition, statements, ctx);
            }
            return null;
        }
    }
    
    private static class ExpressionVisitor extends BulletBaseVisitor<BExpression> {
        public BExpression visitInteger(BulletParser.IntegerContext ctx) {
            if (ctx == null) {
                return null;
            }
            return new BExpression(Integer.parseInt(ctx.INTEGER().getText()), ctx);
        }
        public BExpression visitFloat(BulletParser.FloatContext ctx) {
            if (ctx == null) {
                return null;
            }
            return new BExpression(Float.parseFloat(ctx.FLOAT().getText()), ctx);
        }
        public BExpression visitString(BulletParser.StringContext ctx) {
            if (ctx == null) {
                return null;
            }
            return new BExpression(ctx.STRING().getText(), ctx);
        }
        public BExpression visitLiteralString(BulletParser.LiteralStringContext ctx) {
            if (ctx == null) {
                return null;
            }
            return new BExpression(ctx.LIT_STRING().getText(), ctx);
        }
        public BExpression visitReference(BulletParser.ReferenceContext ctx) {
            if (ctx == null) {
                return null;
            }
            String name = ctx.IDENTIFIER().getText();
            BExpression expression = new BExpression(name, ctx);
            if ((ctx.RP() != null && ctx.LP() != null) || ctx.funcParams() != null) {
                expression.isFunctionReference = true;
                expression.arguments.addAll(funcParamsVisitor.visitFuncParams(ctx.funcParams()));
            }
            return expression;
        }
    }
    
    private static class FuncParamsVisitor extends BulletBaseVisitor<List<BExpression>> {
        public List<BExpression> visitFuncParams(BulletParser.FuncParamsContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<BExpression> expressions = visitFuncParams(ctx.funcParams());
            BExpression expression = expressionVisitor.visit(ctx.expression());
            if (expression != null) {
                expressions.add(0, expression);
            }
            return expressions;
        }
    }
    
}
