package com.cjburkey.bullet.visitor;

import com.cjburkey.bullet.antlr.BulletBaseVisitor;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AArrayType;
import com.cjburkey.bullet.parser.AIfStatement;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.AOperator;
import com.cjburkey.bullet.parser.ATypeDec;
import com.cjburkey.bullet.parser.ATypes;
import com.cjburkey.bullet.parser.AVariableAssign;
import com.cjburkey.bullet.parser.AVariableDec;
import com.cjburkey.bullet.parser.AVariableRef;
import com.cjburkey.bullet.parser.classDec.AClassDec;
import com.cjburkey.bullet.parser.classDec.AClassMembers;
import com.cjburkey.bullet.parser.expression.AArrayValue;
import com.cjburkey.bullet.parser.expression.ABinaryOperator;
import com.cjburkey.bullet.parser.expression.ABoolean;
import com.cjburkey.bullet.parser.AExprList;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.parser.expression.AFloat;
import com.cjburkey.bullet.parser.expression.AInteger;
import com.cjburkey.bullet.parser.AReference;
import com.cjburkey.bullet.parser.expression.AParentChild;
import com.cjburkey.bullet.parser.expression.ARef;
import com.cjburkey.bullet.parser.expression.AString;
import com.cjburkey.bullet.parser.expression.AUnaryOperator;
import com.cjburkey.bullet.parser.function.AArgument;
import com.cjburkey.bullet.parser.function.AArguments;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import com.cjburkey.bullet.parser.namespace.ANamespace;
import com.cjburkey.bullet.parser.namespace.ANamespaceIn;
import com.cjburkey.bullet.parser.program.AProgram;
import com.cjburkey.bullet.parser.program.AProgramIn;
import com.cjburkey.bullet.parser.program.ARequirement;
import com.cjburkey.bullet.parser.program.ARequirements;
import com.cjburkey.bullet.parser.statement.AScope;
import com.cjburkey.bullet.parser.statement.AStatement;
import com.cjburkey.bullet.parser.statement.AStatementExpression;
import com.cjburkey.bullet.parser.statement.AStatementIf;
import com.cjburkey.bullet.parser.statement.AStatementReturn;
import com.cjburkey.bullet.parser.statement.AStatementVariableAssign;
import com.cjburkey.bullet.parser.statement.AStatementVariableDec;
import java.util.Optional;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class ParserVisitor {
    
    // Visitor instances (to prevent repeat instantiation)
    public static final ProgramVisitor _programVisitor = new ProgramVisitor();
    public static final RequirementsVisitor _requirementsVisitor = new RequirementsVisitor();
    public static final RequirementVisitor _requirementVisitor = new RequirementVisitor();
    public static final ProgramInVisitor _programInVisitor = new ProgramInVisitor();
    public static final NamespaceVisitor _namespaceVisitor = new NamespaceVisitor();
    public static final NameVisitor _nameVisitor = new NameVisitor();
    public static final NamespaceInVisitor _namespaceInVisitor = new NamespaceInVisitor();
    public static final VariableDecVisitor _variableDecVisitor = new VariableDecVisitor();
    public static final VariableRefVisitor _variableRefVisitor = new VariableRefVisitor();
    public static final TypeDecVisitor _typeDecVisitor = new TypeDecVisitor();
    public static final AArrayTypeVisitor _arrayTypeVisitor = new AArrayTypeVisitor();
    public static final ExpressionVisitor _expressionVisitor = new ExpressionVisitor();
    public static final BooleanVisitor _booleanVisitor = new BooleanVisitor();
    public static final IntegerVisitor _integerVisitor = new IntegerVisitor();
    public static final FloatVisitor _floatVisitor = new FloatVisitor();
    public static final StringVisitor _stringVisitor = new StringVisitor();
    public static final UnaryOpVisitor _unaryOpVisitor = new UnaryOpVisitor();
    public static final BinaryOpVisitor _binaryOpVisitor = new BinaryOpVisitor();
    public static final RefVisitor _refVisitor = new RefVisitor();
    public static final ReferenceVisitor _referenceVisitor = new ReferenceVisitor();
    public static final AParentChildVisitor _parentChildVisitor = new AParentChildVisitor();
    public static final ArrayValueVisitor _arrayValueVisitor = new ArrayValueVisitor();
    public static final ExprListVisitor _exprListVisitor = new ExprListVisitor();
    public static final FunctionDecVisitor _functionDecVisitor = new FunctionDecVisitor();
    public static final ArgumentsVisitor _argumentsVisitor = new ArgumentsVisitor();
    public static final ArgumentVisitor _argumentVisitor = new ArgumentVisitor();
    public static final ScopeVisitor _scopeVisitor = new ScopeVisitor();
    public static final StatementVisitor _statementVisitor = new StatementVisitor();
    public static final StatementVariableDecVisitor _statementVariableDecVisitor = new StatementVariableDecVisitor();
    public static final StatementVariableAssignVisitor _statementVariableAssignVisitor = new StatementVariableAssignVisitor();
    public static final VariableAssignVisitor _variableAssignVisitor = new VariableAssignVisitor();
    public static final StatementIfVisitor _statementIfVisitor = new StatementIfVisitor();
    public static final IfStatementVisitor _ifStatementVisitor = new IfStatementVisitor();
    public static final StatementExpressionVisitor _statementExpressionVisitor = new StatementExpressionVisitor();
    public static final StatementReturnVisitor _statementReturnVisitor = new StatementReturnVisitor();
    public static final ClassDecVisitor _classDecVisitor = new ClassDecVisitor();
    public static final TypesVisitor _typesVisitor = new TypesVisitor();
    public static final ClassMembersVisitor _classMembersVisitor = new ClassMembersVisitor();
    
    // Calls the program visitor on the supplied program context
    public static Optional<AProgram> parseProgram(BulletParser.ProgramContext ctx) {
        return _programVisitor.visit(ctx);
    }
    
    // Visit the outer program, this starts the entire parse process
    public static final class ProgramVisitor extends B<AProgram> {
        public Optional<AProgram> visitProgram(BulletParser.ProgramContext ctx) {
            final Optional<ARequirements> requirements = _requirementsVisitor.visit(ctx.requirements());
            final Optional<AProgramIn> programIn = _programInVisitor.visit(ctx.programIn());
            return Optional.of(new AProgram(requirements, programIn, ctx));
        }
    }
    
    // Visit a list of requirements in the input program
    public static final class RequirementsVisitor extends B<ARequirements> {
        public Optional<ARequirements> visitRequirements(BulletParser.RequirementsContext ctx) {
            // No null check needed for requirements because our custom base class will return Optional.empty() if the
            // input is null
            final ARequirements requirements = visit(ctx.requirements()).orElseGet(() -> new ARequirements(ctx));
            _requirementVisitor.visit(ctx.requirement()).ifPresent(requirement -> requirements.requirements.add(0, requirement));
            return Optional.of(requirements);
        }
    }
    
    // Visit a single requirement inside of a list of requirements
    public static final class RequirementVisitor extends B<ARequirement> {
        public Optional<ARequirement> visitRequirement(BulletParser.RequirementContext ctx) {
            // This dank Optional.map function allows us to check if STRING() returns null and return a
            // requirement based on that, rather than a separate if statement/ternary operator
            return Optional.ofNullable(ctx.STRING()).map(string -> new ARequirement(string.getText(), ctx));
        }
    }
    
    // Visit the contents of the program
    public static final class ProgramInVisitor extends B<AProgramIn> {
        @SuppressWarnings("OptionalAssignedToNull")
        public Optional<AProgramIn> visitProgramIn(BulletParser.ProgramInContext ctx) {
            final AProgramIn programIn = visit(ctx.programIn()).orElseGet(() -> new AProgramIn(ctx));
            _namespaceVisitor.visit(ctx.namespace()).ifPresent(namespace -> programIn.namespaces.add(0, namespace));
            _functionDecVisitor.visit(ctx.functionDec()).ifPresent(functionDec -> programIn.functions.add(0, functionDec));
            _classDecVisitor.visit(ctx.classDec()).ifPresent(classDec -> programIn.classes.add(0, classDec));
            Optional<AStatement> statementOptional = _statementVisitor.visit(ctx.statement());
            if (statementOptional != null) {    // This can happen after a syntax error
                statementOptional.ifPresent(statement -> programIn.statements.add(0, statement));
            }
            return Optional.of(programIn);
        }
    }
    
    // Visit a namespace declaration and its contents
    public static final class NamespaceVisitor extends B<ANamespace> {
        public Optional<ANamespace> visitNamespace(BulletParser.NamespaceContext ctx) {
            final Optional<AName> name = _nameVisitor.visit(ctx.name());
            final Optional<ANamespaceIn> namespaceIn = _namespaceInVisitor.visit(ctx.namespaceIn());
            if (name.isPresent() && namespaceIn.isPresent()) {
                return Optional.of(new ANamespace(name.get(), namespaceIn.get(), ctx));
            }
            return Optional.empty();
        }
    }
    
    public static final class NameVisitor extends B<AName> {
        public Optional<AName> visitName(BulletParser.NameContext ctx) {
            return Optional.ofNullable(ctx.IDENTIFIER()).map(identifier -> new AName(identifier.getText(), ctx));
        }
    }
    
    public static final class NamespaceInVisitor extends B<ANamespaceIn> {
        public Optional<ANamespaceIn> visitNamespaceIn(BulletParser.NamespaceInContext ctx) {
            final ANamespaceIn namespaceIn = _namespaceInVisitor.visit(ctx.namespaceIn()).orElseGet(() -> new ANamespaceIn(ctx));
            _variableDecVisitor.visit(ctx.variableDec()).ifPresent(namespaceIn.variableDecs::add);
            _functionDecVisitor.visit(ctx.functionDec()).ifPresent(namespaceIn.functionDecs::add);
            _classDecVisitor.visit(ctx.classDec()).ifPresent(namespaceIn.classDecs::add);
            return Optional.of(namespaceIn);
        }
    }
    
    public static final class VariableDecVisitor extends B<AVariableDec> {
        public Optional<AVariableDec> visitVariableDec(BulletParser.VariableDecContext ctx) {
            final Optional<AVariableRef> variableRef = _variableRefVisitor.visit(ctx.variableRef());
            final Optional<ATypeDec> typeDec = _typeDecVisitor.visit(ctx.typeDec());
            final Optional<AExpression> expression = _expressionVisitor.visit(ctx.expression());
            // Variable name is the only thing "required" during parsing (during compilation, if neither
            // typeDec or expression are present, then there will be problems)
            return variableRef.map(variable -> new AVariableDec(variable, typeDec, expression, ctx));
        }
    }
    
    public static final class VariableRefVisitor extends B<AVariableRef> {
        public Optional<AVariableRef> visitVariableRef(BulletParser.VariableRefContext ctx) {
            final int variableType = ctx.VAR_TYPE() == null ? 0 : ctx.VAR_TYPE().getText().length();
            return _nameVisitor.visit(ctx.name()).map(name -> new AVariableRef(variableType, name, ctx));
        }
    }
    
    public static final class TypeDecVisitor extends B<ATypeDec> {
        public Optional<ATypeDec> visitTypeDec(BulletParser.TypeDecContext ctx) {
            return Optional.ofNullable(ctx.IDENTIFIER()).map(identifier -> new ATypeDec(identifier.getText(),
                    _arrayTypeVisitor.visit(ctx.arrayType()), ctx));
        }
    }
    
    public static final class AArrayTypeVisitor extends B<AArrayType> {
        public Optional<AArrayType> visitArrayType(BulletParser.ArrayTypeContext ctx) {
            return Optional.of(new AArrayType(_expressionVisitor.visit(ctx.expression()), ctx));
        }
    }
    
    public static final class ExpressionVisitor extends B<AExpression> {
        // We have to map the visits to get the inheriting classes of AExpression
        // to become Optional<AExpression> as Optional<ABoolean>, etc, do not
        // inherit from Optional<AExpression>
        public Optional<AExpression> visitBoolean(BulletParser.BooleanContext ctx) {
            return _booleanVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AExpression> visitInteger(BulletParser.IntegerContext ctx) {
            return _integerVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AExpression> visitFloat(BulletParser.FloatContext ctx) {
            return _floatVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AExpression> visitString(BulletParser.StringContext ctx) {
            return _stringVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AExpression> visitLiteralString(BulletParser.LiteralStringContext ctx) {
            return _stringVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AExpression> visitUnaryOp(BulletParser.UnaryOpContext ctx) {
            return _unaryOpVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AExpression> visitBinaryOp(BulletParser.BinaryOpContext ctx) {
            return _binaryOpVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AExpression> visitParenthesisWrap(BulletParser.ParenthesisWrapContext ctx) {
            return visit(ctx.expression());
        }
        public Optional<AExpression> visitRef(BulletParser.RefContext ctx) {
            return _refVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AExpression> visitParentChild(BulletParser.ParentChildContext ctx) {
            return _parentChildVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AExpression> visitPartialExp(BulletParser.PartialExpContext ctx) {
            return visit(ctx.expression());
        }
        public Optional<AExpression> visitArrayValue(BulletParser.ArrayValueContext ctx) {
            return _arrayValueVisitor.visit(ctx).map(val -> val);
        }
    }
    
    public static final class BooleanVisitor extends B<ABoolean> {
        public Optional<ABoolean> visitBoolean(BulletParser.BooleanContext ctx) {
            return Optional.ofNullable(ctx.BOOL()).map(bool -> new ABoolean(Boolean.parseBoolean(bool.getText()), ctx));
        }
    }
    
    public static final class IntegerVisitor extends B<AInteger> {
        public Optional<AInteger> visitInteger(BulletParser.IntegerContext ctx) {
            return Optional.ofNullable(ctx.INTEGER()).map(integer -> new AInteger(integer.getText(), ctx));
        }
    }
    
    public static final class FloatVisitor extends B<AFloat> {
        public Optional<AFloat> visitFloat(BulletParser.FloatContext ctx) {
            return Optional.ofNullable(ctx.FLOAT()).map(floating -> new AFloat(floating.getText(), ctx));
        }
    }
    
    public static final class StringVisitor extends B<AString> {
        public Optional<AString> visitString(BulletParser.StringContext ctx) {
            Optional<String> string = Optional.ofNullable(ctx.STRING()).map(ParseTree::getText);
            return string.map(aString -> new AString(aString.startsWith("@"), aString, ctx));
        }
        public Optional<AString> visitLiteralString(BulletParser.LiteralStringContext ctx) {
            return Optional.ofNullable(ctx.LIT_STRING()).map(string -> new AString(string.getText(), ctx));
        }
    }
    
    public static final class UnaryOpVisitor extends B<AUnaryOperator> {
        public Optional<AUnaryOperator> visitUnaryOp(BulletParser.UnaryOpContext ctx) {
            final Optional<AExpression> expression = _expressionVisitor.visit(ctx.expression());
            final Optional<AOperator> operator = AOperator.from(ctx);
            if (expression.isPresent() && operator.isPresent()) {
                return Optional.of(new AUnaryOperator(expression.get(), operator.get(), ctx));
            }
            return Optional.empty();
        }
    }
    
    public static final class BinaryOpVisitor extends B<ABinaryOperator> {
        public Optional<ABinaryOperator> visitBinaryOp(BulletParser.BinaryOpContext ctx) {
            if (ctx.expression().size() != 2) {
                return Optional.empty();
            }
            final Optional<AExpression> expressionA = _expressionVisitor.visit(ctx.expression(0));
            final Optional<AExpression> expressionB = _expressionVisitor.visit(ctx.expression(1));
            final Optional<AOperator> operator = AOperator.from(ctx);
            if (expressionA.isPresent() && expressionB.isPresent() && operator.isPresent()) {
                return Optional.of(new ABinaryOperator(expressionA.get(), expressionB.get(), operator.get(), ctx));
            }
            return Optional.empty();
        }
    }
    
    public static final class RefVisitor extends B<ARef> {
        public Optional<ARef> visitRef(BulletParser.RefContext ctx) {
            return _referenceVisitor.visit(ctx.reference()).map(reference -> new ARef(reference, ctx));
        }
    }
    
    public static final class ReferenceVisitor extends B<AReference> {
        public Optional<AReference> visitAmbigReference(BulletParser.AmbigReferenceContext ctx) {
            final Optional<AVariableRef> variableRef = _variableRefVisitor.visit(ctx.variableRef());
            return variableRef.map(variable -> new AReference(variable, ctx));
        }
        public Optional<AReference> visitFunctionReference(BulletParser.FunctionReferenceContext ctx) {
            final Optional<AName> name = _nameVisitor.visit(ctx.name());
            final Optional<AExprList> funcParams = _exprListVisitor.visit(ctx.exprList());
            final Optional<AOperator> operator = AOperator.from(ctx.op());
            return operator.map(aOperator -> Optional.of(new AReference(aOperator, funcParams, ctx)))
                    .orElseGet(() -> name.map(aName -> new AReference(aName, funcParams, ctx)));
        }
    }
    
    public static final class AParentChildVisitor extends B<AParentChild> {
        public Optional<AParentChild> visitParentChild(BulletParser.ParentChildContext ctx) {
            Optional<AExpression> expression = _expressionVisitor.visit(ctx.expression());
            Optional<AReference> reference = _referenceVisitor.visit(ctx.reference());
            if (expression.isPresent() && reference.isPresent()) {
                return Optional.of(new AParentChild(expression.get(), reference.get(), ctx));
            }
            return Optional.empty();
        }
    }
    
    public static final class ExprListVisitor extends B<AExprList> {
        public Optional<AExprList> visitExprList(BulletParser.ExprListContext ctx) {
            final AExprList funcParams = visit(ctx.exprList()).orElseGet(() -> new AExprList(ctx));
            _expressionVisitor.visit(ctx.expression()).ifPresent(expression -> funcParams.expressions.add(0, expression));
            return Optional.of(funcParams);
        }
    }
    
    public static final class ArrayValueVisitor extends B<AArrayValue> {
        public Optional<AArrayValue> visitArrayValue(BulletParser.ArrayValueContext ctx) {
            return _exprListVisitor.visit(ctx.exprList()).map(aExprList -> new AArrayValue(aExprList, ctx));
        }
    }
    
    public static final class FunctionDecVisitor extends B<AFunctionDec> {
        public Optional<AFunctionDec> visitFunctionDec(BulletParser.FunctionDecContext ctx) {
            final Optional<AName> name = _nameVisitor.visit(ctx.name());
            final Optional<AOperator> operator = AOperator.from(ctx.op());
            final Optional<AArguments> arguments = _argumentsVisitor.visit(ctx.arguments());
            final Optional<AScope> statements = _scopeVisitor.visit(ctx.scope());
            if (name.isPresent() || operator.isPresent()) {
                return Optional.of(new AFunctionDec(name, operator, arguments, statements, ctx));
            }
            return Optional.empty();
        }
    }
    
    public static final class ArgumentsVisitor extends B<AArguments> {
        public Optional<AArguments> visitArguments(BulletParser.ArgumentsContext ctx) {
            final AArguments arguments = visit(ctx.arguments()).orElseGet(() -> new AArguments(ctx));
            _argumentVisitor.visit(ctx.argument()).ifPresent(argument -> arguments.arguments.add(0, argument));
            return Optional.of(arguments);
        }
    }
    
    public static final class ArgumentVisitor extends B<AArgument> {
        public Optional<AArgument> visitArgument(BulletParser.ArgumentContext ctx) {
            final Optional<AName> name = _nameVisitor.visit(ctx.name());
            final Optional<ATypeDec> typeDec = _typeDecVisitor.visit(ctx.typeDec());
            return name.map(aName -> new AArgument(aName, typeDec, ctx));
        }
    }
    
    public static final class ScopeVisitor extends B<AScope> {
        public Optional<AScope> visitScope(BulletParser.ScopeContext ctx) {
            final AScope statements = visit(ctx.scope()).orElseGet(() -> new AScope(ctx));
            _statementVisitor.visit(ctx.statement()).ifPresent(statement -> statements.statements.add(0, statement));
            return Optional.of(statements);
        }
    }
    
    public static final class StatementVisitor extends B<AStatement> {
        public Optional<AStatement> visitStatementVariableDec(BulletParser.StatementVariableDecContext ctx) {
            return _statementVariableDecVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AStatement> visitStatementVariableAssign(BulletParser.StatementVariableAssignContext ctx) {
            return _statementVariableAssignVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AStatement> visitStatementIf(BulletParser.StatementIfContext ctx) {
            return _statementIfVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AStatement> visitStatementExpression(BulletParser.StatementExpressionContext ctx) {
            return _statementExpressionVisitor.visit(ctx).map(val -> val);
        }
        public Optional<AStatement> visitStatementReturn(BulletParser.StatementReturnContext ctx) {
            return _statementReturnVisitor.visit(ctx).map(val -> val);
        }
    }
    
    public static final class StatementVariableDecVisitor extends B<AStatementVariableDec> {
        public Optional<AStatementVariableDec> visitStatementVariableDec(BulletParser.StatementVariableDecContext ctx) {
            return _variableDecVisitor.visit(ctx.variableDec()).map(variable -> new AStatementVariableDec(variable, ctx));
        }
    }
    
    public static final class StatementVariableAssignVisitor extends B<AStatementVariableAssign> {
        public Optional<AStatementVariableAssign> visitStatementVariableAssign(BulletParser.StatementVariableAssignContext ctx) {
            return _variableAssignVisitor.visit(ctx.variableAssign()).map(variable -> new AStatementVariableAssign(variable, ctx));
        }
    }
    
    public static final class VariableAssignVisitor extends B<AVariableAssign> {
        public Optional<AVariableAssign> visitVariableAssign(BulletParser.VariableAssignContext ctx) {
            final Optional<AVariableRef> variableRef = _variableRefVisitor.visit(ctx.variableRef());
            final Optional<AExpression> expression = _expressionVisitor.visit(ctx.expression());
            if (variableRef.isPresent() && expression.isPresent()) {
                return Optional.of(new AVariableAssign(variableRef.get(), expression.get(), ctx));
            }
            return Optional.empty();
        }
    }
    
    public static final class StatementIfVisitor extends B<AStatementIf> {
        public Optional<AStatementIf> visitStatementIf(BulletParser.StatementIfContext ctx) {
            return _ifStatementVisitor.visit(ctx.ifStatement()).map(ifStatement -> new AStatementIf(ifStatement, ctx));
        }
    }
    
    public static final class IfStatementVisitor extends B<AIfStatement> {
        public Optional<AIfStatement> visitIfStatement(BulletParser.IfStatementContext ctx) {
            final Optional<AExpression> expression = _expressionVisitor.visit(ctx.expression());
            final Optional<AScope> statements;
            if (ctx.scope() != null) {
                statements = _scopeVisitor.visit(ctx.scope());
            } else if (ctx.statement() != null) {
                statements = Optional.of(new AScope(ctx));
                _statementVisitor.visit(ctx.statement()).ifPresent(statements.get().statements::add);
            } else {
                statements = Optional.empty();
            }
            return statements.map(aScope -> new AIfStatement(ctx.ELSE() != null, expression, aScope, ctx));
        }
    }
    
    public static final class StatementExpressionVisitor extends B<AStatementExpression> {
        public Optional<AStatementExpression> visitStatementExpression(BulletParser.StatementExpressionContext ctx) {
            return _expressionVisitor.visit(ctx.expression()).map(expression -> new AStatementExpression(expression, ctx));
        }
    }
    
    public static final class StatementReturnVisitor extends B<AStatementReturn> {
        public Optional<AStatementReturn> visitStatementReturn(BulletParser.StatementReturnContext ctx) {
            if (ctx.expression() == null) return Optional.empty();
            final Optional<AExpression> exp = _expressionVisitor.visit(ctx.expression());
            //noinspection OptionalAssignedToNull
            if (exp == null) return Optional.empty();   // Weird possibility somehow
            return exp.map(expression -> new AStatementReturn(expression, ctx));
        }
    }
    
    public static final class ClassDecVisitor extends B<AClassDec> {
        public Optional<AClassDec> visitClassDec(BulletParser.ClassDecContext ctx) {
            final Optional<AName> name = _nameVisitor.visit(ctx.name());
            final Optional<ATypes> types = _typesVisitor.visit(ctx.types());
            final Optional<AClassMembers> classMembers = _classMembersVisitor.visit(ctx.classMembers());
            return name.map(aName -> new AClassDec(aName, types, classMembers, ctx));
        }
    }
    
    public static final class TypesVisitor extends B<ATypes> {
        public Optional<ATypes> visitTypes(BulletParser.TypesContext ctx) {
            final ATypes types = visit(ctx.types()).orElseGet(() -> new ATypes(ctx));
            Optional.ofNullable(ctx.IDENTIFIER()).ifPresent(identifier -> types.types.add(identifier.getText()));
            return Optional.of(types);
        }
    }
    
    public static final class ClassMembersVisitor extends B<AClassMembers> {
        public Optional<AClassMembers> visitClassMembers(BulletParser.ClassMembersContext ctx) {
            final AClassMembers types = visit(ctx.classMembers()).orElseGet(() -> new AClassMembers(ctx));
            _variableDecVisitor.visit(ctx.variableDec()).ifPresent(variableDec -> types.variableDecs.add(0, variableDec));
            _functionDecVisitor.visit(ctx.functionDec()).ifPresent(functionDec -> types.functionDecs.add(0, functionDec));
            return Optional.of(types);
        }
    }
    
    // The base, will allow handling of actions during any visit call
    // Could be used to detect past errors, etc.
    public static class B<T> extends BulletBaseVisitor<Optional<T>> {
        public Optional<T> visit(final ParseTree tree) {
            if (tree == null) {
                return Optional.empty();
            }
            return tree.accept(this);
        }
    }
    
}
