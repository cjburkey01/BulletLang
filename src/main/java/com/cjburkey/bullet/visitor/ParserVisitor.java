package com.cjburkey.bullet.visitor;

import com.cjburkey.bullet.Log;
import com.cjburkey.bullet.antlr.BulletBaseVisitor;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.BFunction;
import com.cjburkey.bullet.obj.BNamespace;
import com.cjburkey.bullet.obj.BOperator;
import com.cjburkey.bullet.obj.BProgram;
import com.cjburkey.bullet.obj.classdef.BClass;
import com.cjburkey.bullet.obj.classdef.BVariableType;
import com.cjburkey.bullet.obj.classdef.IBClassMember;
import com.cjburkey.bullet.obj.statement.BArgument;
import com.cjburkey.bullet.obj.statement.BExpressionStatement;
import com.cjburkey.bullet.obj.statement.BIfStatement;
import com.cjburkey.bullet.obj.statement.BReturnStatement;
import com.cjburkey.bullet.obj.statement.BStatement;
import com.cjburkey.bullet.obj.statement.BVariable;
import com.cjburkey.bullet.visitor.struct.Content;
import com.cjburkey.bullet.visitor.struct.ProgramIn;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class ParserVisitor {
    
    private static final ProgramVisitor programVisitor = new ProgramVisitor();
    private static final RequirementsVisitor requirementsVisitor = new RequirementsVisitor();
    private static final ContentVisitor contentVisitor = new ContentVisitor();
    private static final NamespaceVisitor namespaceVisitor = new NamespaceVisitor();
    private static final NamespaceInVisitor namespaceInVisitor = new NamespaceInVisitor();
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
    private static final ClassDefVisitor classDefVisitor = new ClassDefVisitor();
    private static final TypesVisitor typesVisitor = new TypesVisitor();
    private static final ClassMembersVisitor classMembersVisitor = new ClassMembersVisitor();
    private static final AttribInVisitor attribInVisitor = new AttribInVisitor();
    
    public static boolean stop = false;
    
    public static BProgram parse(BulletParser parser) {
        return programVisitor.visit(parser.program());
    }
    
    private static class ProgramVisitor extends BaseV<BProgram> {
        public BProgram visitProgram(BulletParser.ProgramContext ctx) {
            if (ctx == null || ctx.programIn() == null) {
                return null;
            }
            List<String> requiredFiles = ctx.requirements() == null ? new ArrayList<>() : requirementsVisitor.visit(ctx.requirements());
            ProgramIn programContents = programInVisitor.visit(ctx.programIn());
            return new BProgram(requiredFiles, programContents, ctx);
        }
    }
    
    private static class RequirementsVisitor extends BaseV<List<String>> {
        public List<String> visitRequirements(BulletParser.RequirementsContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<String> requirements = ctx.requirements() == null ? new ArrayList<>() : visit(ctx.requirements());
            if (ctx.requirement() != null && ctx.requirement().STRING() != null) {
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
    
    private static class ProgramInVisitor extends BaseV<ProgramIn> {
        public ProgramIn visitProgramIn(BulletParser.ProgramInContext ctx) {
            if (ctx == null) {
                return new ProgramIn();
            }
            ProgramIn programIn = ctx.programIn() == null ? new ProgramIn() : visit(ctx.programIn());
            if (ctx.namespace() != null) {
                programIn.namespaces.add(0, namespaceVisitor.visit(ctx.namespace()));
            } else if (ctx.statement() != null) {
                programIn.statements.add(0, statementVisitor.visit(ctx.statement()));
            } else if (ctx.content() != null) {
                programIn.contents.add(0, contentVisitor.visit(ctx.content()));
            }
            return programIn;
        }
    }
    
    private static class ContentVisitor extends BaseV<Content> {
        public Content visitContent(BulletParser.ContentContext ctx) {
            if (ctx == null) {
                return null;
            }
            Content content = new Content();
            content.function = ctx.function() != null ? functionVisitor.visit(ctx.function()) : null;
            content.classDec = ctx.classDef() != null ? classDefVisitor.visit(ctx.classDef()) : null;
            return content;
        }
    }
    
    private static class NamespaceVisitor extends BaseV<BNamespace> {
        public BNamespace visitNamespace(BulletParser.NamespaceContext ctx) {
            if (ctx == null || ctx.IDENTIFIER() == null) {
                return null;
            }
            String name = ctx.IDENTIFIER().getText();
            List<Content> contents = ctx.namespaceIn() == null ? new ArrayList<>() : namespaceInVisitor.visit(ctx.namespaceIn());
            return new BNamespace(name, contents, ctx);
        }
    }
    
    private static class NamespaceInVisitor extends BaseV<List<Content>> {
        public List<Content> visitNamespaceIn(BulletParser.NamespaceInContext ctx) {
            if (ctx == null) {
                return null;
            }
            List<Content> contents = ctx.namespaceIn() == null ? new ArrayList<>() : namespaceInVisitor.visit(ctx.namespaceIn());
            Content content = ctx.content() != null ? contentVisitor.visit(ctx.content()) : null;
            if (content != null) {
                contents.add(0, content);
            }
            return contents;
        }
    }
    
    private static class FunctionVisitor extends BaseV<BFunction> {
        public BFunction visitFunction(BulletParser.FunctionContext ctx) {
            if (ctx == null) {
                return null;
            }
            List<String> attribs = ctx.attrib() == null ? new ArrayList<>() : attribInVisitor.visitAttribIn(ctx.attrib().attribIn());
            
            String name = null;
            if (ctx.IDENTIFIER() != null) {
                name = ctx.IDENTIFIER().getText();
            }
            if (ctx.PLUS() != null) {
                name = ctx.PLUS().getText();
            }
            if (ctx.MINUS() != null) {
                name = ctx.MINUS().getText();
            }
            if (ctx.TIMES() != null) {
                name = ctx.TIMES().getText();
            }
            if (ctx.DIV() != null) {
                name = ctx.DIV().getText();
            }
            if (ctx.POW() != null) {
                name = ctx.POW().getText();
            }
            if (ctx.ROOT() != null) {
                name = ctx.ROOT().getText();
            }
            
            String type = (ctx.typeDef() != null && ctx.typeDef().typeName() != null) ? ctx.typeDef().typeName().getText() : null;
            List<BArgument> arguments = ctx.arguments() == null ? new ArrayList<>() : argumentsVisitor.visit(ctx.arguments());
            List<BStatement> statements = ctx.statements() == null ? new ArrayList<>() : statementsVisitor.visit(ctx.statements());
            return new BFunction(attribs, name, type, arguments, statements, ctx);
        }
    }
    
    private static class AttribInVisitor extends BaseV<List<String>> {
        public List<String> visitAttribIn(BulletParser.AttribInContext ctx) {
            if (ctx == null || ctx.IDENTIFIER() == null || ctx.IDENTIFIER().getText() == null) {
                return new ArrayList<>();
            }
            List<String> attribs = ctx.attribIn() == null ? new ArrayList<>() : visit(ctx.attribIn());
            String attrib = ctx.IDENTIFIER().getText();
            if (attrib != null) {
                attribs.add(0, attrib);
            }
            return attribs;
        }
    }
    
    private static class ArgumentsVisitor extends BaseV<List<BArgument>> {
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
    
    private static class ArgumentVisitor extends BaseV<BArgument> {
        public BArgument visitArgument(BulletParser.ArgumentContext ctx) {
            if (ctx == null) {
                return null;
            }
            String name = ctx.IDENTIFIER().getText();
            String type = (ctx.typeDef() != null && ctx.typeDef().typeName() != null) ? ctx.typeDef().typeName().getText() : null;
            return new BArgument(name, type, ctx);
        }
    }
    
    private static class StatementsVisitor extends BaseV<List<BStatement>> {
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
    
    private static class StatementVisitor extends BaseV<BStatement> {
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
        public BStatement visitStatementReturn(BulletParser.StatementReturnContext ctx) {
            BExpression expression = expressionVisitor.visit(ctx.expression());
            if (expression != null) {
                return new BReturnStatement(expression, ctx);
            }
            return null;
        }
    }
    
    private static class VariableDefVisitor extends BaseV<BVariable> {
        public BVariable visitVariableDef(BulletParser.VariableDefContext ctx) {
            if (ctx == null) {
                return null;
            }
            BVariableType variableType = (ctx.VAR_TYPE() == null || ctx.VAR_TYPE().getText() == null) ? BVariableType.STANDARD : BVariableType.get(ctx.VAR_TYPE().getText().length());
            String name = ctx.IDENTIFIER().getText();
            String type = (ctx.typeDef() != null && ctx.typeDef().typeName() != null) ? ctx.typeDef().typeName().getText() : null;
            BExpression value = ctx.expression() != null ? expressionVisitor.visit(ctx.expression()) : null;
            boolean isDeclaration = ctx.DEC() != null || ctx.COLON() != null;
            return new BVariable(isDeclaration, name, type, variableType, value, ctx);
        }
    }
    
    private static class IfStatementVisitor extends BaseV<BIfStatement> {
        public BIfStatement visitIfStatement(BulletParser.IfStatementContext ctx) {
            if (ctx == null) {
                return null;
            }
            if (ctx.expression() != null || ctx.ELSE() != null) {
                BExpression condition = ctx.expression() == null ? null : expressionVisitor.visit(ctx.expression());
                List<BStatement> statements = ctx.statements() == null ? new ArrayList<>() : statementsVisitor.visit(ctx.statements());
                return new BIfStatement(ctx.ELSE() != null, condition, statements, ctx);
            }
            return null;
        }
    }
    
    private static class ExpressionVisitor extends BaseV<BExpression> {
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
            BExpression parent = null;
            if (ctx.expression() != null) {
                parent = visit(ctx.expression());
            }
            BExpression expression = new BExpression(parent, name, ctx);
            if ((ctx.RP() != null && ctx.LP() != null) || ctx.funcParams() != null) {
                expression.isFunctionReference = true;
                List<BExpression> params = funcParamsVisitor.visit(ctx.funcParams());
                if (params != null) {
                    expression.arguments.addAll(params);
                }
            } else if (ctx.VAR_TYPE() != null) {
                expression.isVariableReference = true;
            }
            return expression;
        }
        public BExpression visitUnaryOp(BulletParser.UnaryOpContext ctx) {
            if (ctx == null || ctx.expression() == null) {
                return null;
            }
            if (ctx.POW() != null) {
                return new BExpression(BOperator.POW, visit(ctx.expression()), ctx);
            }
            if (ctx.ROOT() != null) {
                return new BExpression(BOperator.ROOT, visit(ctx.expression()), ctx);
            }
            if (ctx.MINUS() != null) {
                return new BExpression(BOperator.MINUS, visit(ctx.expression()), ctx);
            }
            return null;
        }
        public BExpression visitBinaryOp(BulletParser.BinaryOpContext ctx) {
            if (ctx == null || ctx.expression().size() != 2) {
                return null;
            }
            if (ctx.POW() != null) {
                return new BExpression(BOperator.POW, visit(ctx.expression(0)), visit(ctx.expression(1)), ctx);
            }
            if (ctx.ROOT() != null) {
                return new BExpression(BOperator.ROOT, visit(ctx.expression(0)), visit(ctx.expression(1)), ctx);
            }
            if (ctx.TIMES() != null) {
                return new BExpression(BOperator.TIMES, visit(ctx.expression(0)), visit(ctx.expression(1)), ctx);
            }
            if (ctx.DIV() != null) {
                return new BExpression(BOperator.DIV, visit(ctx.expression(0)), visit(ctx.expression(1)), ctx);
            }
            if (ctx.PLUS() != null) {
                return new BExpression(BOperator.PLUS, visit(ctx.expression(0)), visit(ctx.expression(1)), ctx);
            }
            if (ctx.MINUS() != null) {
                return new BExpression(BOperator.MINUS, visit(ctx.expression(0)), visit(ctx.expression(1)), ctx);
            }
            return null;
        }
    }
    
    private static class FuncParamsVisitor extends BaseV<List<BExpression>> {
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
    
    private static class ClassDefVisitor extends BaseV<BClass> {
        public BClass visitClassDef(BulletParser.ClassDefContext ctx) {
            if (ctx == null) {
                return null;
            }
            List<String> types = ctx.types() == null ? new ArrayList<>() : typesVisitor.visit(ctx.types());
            List<IBClassMember> members = ctx.classMembers() == null ? new ArrayList<>() : classMembersVisitor.visit(ctx.classMembers());
            return new BClass(ctx.IDENTIFIER().getText(), false, types, members, ctx);      // TODO: VISIBILITY
        }
    }
    
    private static class TypesVisitor extends BaseV<List<String>> {
        public List<String> visitTypes(BulletParser.TypesContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<String> types = ctx.types() == null ? new ArrayList<>() : visit(ctx.types());
            if (ctx.IDENTIFIER() != null && ctx.IDENTIFIER().getText() != null) {
                types.add(0, ctx.IDENTIFIER().getText());
            }
            return types;
        }
    }
    
    private static class ClassMembersVisitor extends BaseV<List<IBClassMember>> {
        public List<IBClassMember> visitClassMembers(BulletParser.ClassMembersContext ctx) {
            if (ctx == null) {
                return new ArrayList<>();
            }
            List<IBClassMember> members = ctx.classMembers() == null ? new ArrayList<>() : visit(ctx.classMembers());
            if (ctx.function() != null) {
                BFunction method = functionVisitor.visit(ctx.function());
                if (method != null) {
                    members.add(0, method);
                }
            } else if (ctx.variableDef() != null) {
                BVariable variableDef = variableDefVisitor.visit(ctx.variableDef());
                if (variableDef != null) {
                    members.add(0, variableDef);
                }
            }
            return members;
        }
    }
    
    private static class BaseV<T> extends BulletBaseVisitor<T> {
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
