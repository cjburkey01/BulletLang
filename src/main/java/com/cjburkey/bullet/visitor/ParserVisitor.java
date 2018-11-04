package com.cjburkey.bullet.visitor;

import com.cjburkey.bullet.BulletLang;
import com.cjburkey.bullet.Log;
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
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class ParserVisitor {
    
    private static final ProgramVisitor programVisitor = new ProgramVisitor();
    private static final RequirementsVisitor requirementsVisitor = new RequirementsVisitor();
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
    
    public static boolean stop = false;
    
    public static BProgram parse(BulletParser parser) {
        BProgram program = programVisitor.visit(parser.program());
        BulletLang.debugPrint(program);
        return program;
    }
    
    private static class ProgramVisitor extends BaseBulletVisitor<BProgram> {
        public BProgram visitProgram(BulletParser.ProgramContext ctx) {
            String namespace = (ctx.module() != null && ctx.module().IDENTIFIER() != null) ? ctx.module().IDENTIFIER().getText() : "";
            List<String> requiredFiles = ctx.requirements() == null ? new ArrayList<>() : requirementsVisitor.visit(ctx.requirements());
            ProgramIn programContents = programInVisitor.visit(ctx.programIn());
            return new BProgram(namespace, requiredFiles, programContents.functions, programContents.statements, ctx);
        }
    }
    
    private static class RequirementsVisitor extends BaseBulletVisitor<List<String>> {
        public List<String> visitRequirements(BulletParser.RequirementsContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<String> requirements = ctx.requirements() == null ? new ArrayList<>() : visit(ctx.requirements());
            if (ctx.requirement() != null && ctx.requirement().STRING() != null && ctx.requirement().STRING().getText() != null) {
                String requirement = ctx.requirement().STRING().getText();
                requirement = requirement.substring(1, requirement.length() - 1);
                if (!requirement.endsWith(".blt")) {
                    requirement += ".blt";
                }
                requirements.add(0, requirement);
            }
            return requirements;
        }
    }
    
    private static class ProgramInVisitor extends BaseBulletVisitor<ProgramIn> {
        public ProgramIn visitProgramIn(BulletParser.ProgramInContext ctx) {
            if (ctx == null) {
                return new ProgramIn();
            }
            ProgramIn programIn = ctx.programIn() == null ? new ProgramIn() : visit(ctx.programIn());
            if (ctx.function() != null) {
                BFunction function = functionVisitor.visit(ctx.function());
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
    
    private static class FunctionVisitor extends BaseBulletVisitor<BFunction> {
        public BFunction visitFunction(BulletParser.FunctionContext ctx) {
            if (ctx == null) {
                return null;
            }
            String name = ctx.IDENTIFIER() != null ? ctx.IDENTIFIER().getText() : null;
            String type = (ctx.type() != null && ctx.type().IDENTIFIER() != null) ? ctx.type().IDENTIFIER().getText() : null;
            List<BArgument> arguments = ctx.arguments() == null ? new ArrayList<>() : argumentsVisitor.visit(ctx.arguments());
            List<BStatement> statements = ctx.statements() == null ? new ArrayList<>() : statementsVisitor.visit(ctx.statements());
            return new BFunction(name, type, arguments, statements, ctx);
        }
    }
    
    private static class ArgumentsVisitor extends BaseBulletVisitor<List<BArgument>> {
        public List<BArgument> visitArguments(BulletParser.ArgumentsContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<BArgument> arguments = ctx.arguments() == null ? new ArrayList<>() : visit(ctx.arguments());
            BArgument argument = argumentVisitor.visit(ctx.argument());
            if (argument != null) {
                arguments.add(0, argument);
            }
            return arguments;
        }
    }
    
    private static class ArgumentVisitor extends BaseBulletVisitor<BArgument> {
        public BArgument visitArgument(BulletParser.ArgumentContext ctx) {
            if (ctx == null) {
                return null;
            }
            String name = ctx.IDENTIFIER().getText();
            String type = (ctx.type() != null && ctx.type().IDENTIFIER() != null) ? ctx.type().IDENTIFIER().getText() : null;
            return new BArgument(name, type, ctx);
        }
    }
    
    private static class StatementsVisitor extends BaseBulletVisitor<List<BStatement>> {
        public List<BStatement> visitStatements(BulletParser.StatementsContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<BStatement> statements = ctx.statements() == null ? new ArrayList<>() : visit(ctx.statements());
            if (ctx.statement() != null) {
                BStatement statement = statementVisitor.visit(ctx.statement());
                if (statement != null) {
                    statements.add(0, statement);
                }
            }
            return statements;
        }
    }
    
    private static class StatementVisitor extends BaseBulletVisitor<BStatement> {
        public BStatement visitStatementVariableDef(BulletParser.StatementVariableDefContext ctx) {
            return variableDefVisitor.visit(ctx.variableDef());
        }
        public BStatement visitStatementIf(BulletParser.StatementIfContext ctx) {
            return ifStatementVisitor.visit(ctx.ifStatement());
        }
        public BStatement visitStatementExpression(BulletParser.StatementExpressionContext ctx) {
            BExpression expression = expressionVisitor.visit(ctx.expression());
            if (expression != null) {
                return new BExpressionStatement(expression, ctx.expression());
            }
            return null;
        }
    }
    
    private static class VariableDefVisitor extends BaseBulletVisitor<BVariable> {
        public BVariable visitVariableDef(BulletParser.VariableDefContext ctx) {
            if (ctx == null) {
                return null;
            }
            String name = ctx.IDENTIFIER().getText();
            String type = (ctx.type() != null && ctx.type().IDENTIFIER() != null) ? ctx.type().IDENTIFIER().getText() : null;
            BExpression value = (ctx.variableVal() != null && ctx.variableVal().expression() != null) ? expressionVisitor.visit(ctx.variableVal().expression()) : null;
            return new BVariable(name, type, value, ctx);
        }
    }
    
    private static class IfStatementVisitor extends BaseBulletVisitor<BIfStatement> {
        public BIfStatement visitIfStatement(BulletParser.IfStatementContext ctx) {
            if (ctx == null) {
                return null;
            }
            if (ctx.expression() != null) {
                BExpression condition = expressionVisitor.visit(ctx.expression());
                List<BStatement> statements = ctx.statements() == null ? new ArrayList<>() : statementsVisitor.visit(ctx.statements());
                return new BIfStatement(ctx.ELSE() != null, condition, statements, ctx);
            }
            return null;
        }
    }
    
    private static class ExpressionVisitor extends BaseBulletVisitor<BExpression> {
        public BExpression visitParenthesisWrap(BulletParser.ParenthesisWrapContext ctx) {
            if (ctx == null) {
                return null;
            }
            return expressionVisitor.visit(ctx.expression());
        }
        public BExpression visitBoolean(BulletParser.BooleanContext ctx) {
            if (ctx == null) {
                return null;
            }
            return new BExpression(ctx.BOOL().getText().equals("true"), ctx);
        }
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
            return new BExpression(ctx.STRING().getText(), ctx.getText().startsWith("@"), ctx);
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
                expression.arguments.addAll(funcParamsVisitor.visit(ctx.funcParams()));
            }
            return expression;
        }
    }
    
    private static class FuncParamsVisitor extends BaseBulletVisitor<List<BExpression>> {
        public List<BExpression> visitFuncParams(BulletParser.FuncParamsContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<BExpression> expressions = ctx.funcParams() == null ? new ArrayList<>() : visit(ctx.funcParams());
            if (ctx.expression() != null) {
                BExpression expression = expressionVisitor.visit(ctx.expression());
                if (expression != null) {
                    expressions.add(0, expression);
                }
            }
            return expressions;
        }
    }
    
    private static class BaseBulletVisitor<T> extends BulletBaseVisitor<T> {
        public T visit(ParseTree tree) {
            if (stop) {
                Log.error("Parsing failed due to errors");
                return null;
            }
            if (tree == null) {
                return null;
            }
            return tree.accept(this);
        }
    }
    
}
