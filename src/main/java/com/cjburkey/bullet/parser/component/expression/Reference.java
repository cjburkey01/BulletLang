package com.cjburkey.bullet.parser.component.expression;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.InstanceType;
import com.cjburkey.bullet.parser.component.Arguments;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.statement.FunctionDec;
import com.cjburkey.bullet.parser.component.statement.VariableDec;
import java.util.List;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Reference extends Expression {

    private static final String ERROR_INVALID_FUNCTION_REFERENCE = "Failed to resolve function reference for function \"%s\"";
    private static final String ERROR_INVALID_VARIABLE_REFERENCE = "Failed to resolve variable reference for function \"%s\"";
    private static final String ERROR_INVALID_REFERENCE = "Failed to resolve variable/function reference for \"%s\"";

    private String name;
    private Arguments arguments;
    private ReferenceType referenceType;
    private InstanceType instanceType;

    private VariableDec variableReference;
    private FunctionDec functionReference;
    private boolean secondResolve = false;

    private Reference(ParserRuleContext ctx, String name, Arguments arguments, ReferenceType referenceType, InstanceType instanceType) {
        super(ctx);
        this.name = name;
        this.arguments = arguments;
        this.referenceType = referenceType;
        this.instanceType = instanceType;
    }

    private Reference(ParserRuleContext ctx, String name, ReferenceType referenceType, InstanceType instanceType) {
        super(ctx);
        this.name = name;
        this.referenceType = referenceType;
        this.instanceType = instanceType;
    }

    @Override
    public String toString() {
        return String.format("%s reference to%s {%s}%s",
                referenceType,
                ((instanceType == null) ? "" : " " + instanceType),
                name,
                ((arguments == null) ? "" : String.format(" with arguments {%s}", arguments)));
    }

    @Override
    public void resolveTypes() {
        if (variableReference != null && variableReference.type != null) {
            outputType = variableReference.type.type;
        }
        if (functionReference != null && functionReference.type != null) {
            outputType = functionReference.type.type;
        }
    }

    @Override
    public void resolveReferences() {
        switch (referenceType) {
            case FUNCTION:
                if (resolveFunctionReference()/* && secondResolve*/) {
                    BulletError.queueError(ctx, ERROR_INVALID_FUNCTION_REFERENCE, name);
                }
                break;
            case VARIABLE:
                if (resolveVariableReference()) {
                    BulletError.queueError(ctx, ERROR_INVALID_VARIABLE_REFERENCE, name);
                }
                break;
            case AMBIGUOUS:
                if (resolveVariableReference()) {
                    if (resolveFunctionReference()) {
                        BulletError.queueError(ctx, ERROR_INVALID_REFERENCE, name);
                    }
                }
        }
        secondResolve = true;
    }

    private boolean resolveFunctionReference() {
        List<FunctionDec> functionDecs = parentScope.getFunctions(name, true);
        for (FunctionDec functionDec : functionDecs) {
            if (functionDec.parameters.parameters.size() == 0 && arguments == null) {
                functionReference = functionDec;
                return false;
            }
            if (arguments != null && functionDec.parameters.parameters.size() == arguments.arguments.size()) {
                int i = 0;
                for (Expression expression : arguments.arguments) {
                    expression.resolveTypes();
                    if (!expression.outputType.equals(functionDec.parameters.parameters.get(i).type.type)) {
                        break;
                    }
                    i++;
                }
                functionReference = functionDec;
                return false;
            }
        }
        return true;
    }

    private boolean resolveVariableReference() {
        List<VariableDec> variableDecs = parentScope.getVariables(name, true);
        if (variableDecs.size() <= 0) return true;
        variableReference = variableDecs.get(0);
        return false;
    }

    public static final class Visitor extends BaseV<Reference> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<Reference> visitReference(BulletLangParser.ReferenceContext ctx) {
            Arguments arguments = new Arguments.Visitor(scope)
                    .visit(ctx.arguments())
                    .orElse(null);
            String name = ctx.IDENTIFIER().getText();
            ReferenceType type = ReferenceType.getType(ctx);
            InstanceType instanceType = InstanceType.get(ctx.instType());

            Reference reference = ((arguments == null)
                    ? (new Reference(ctx, name, type, instanceType))
                    : (new Reference(ctx, name, arguments, type, instanceType)));
            reference.parentScope = scope;
            return Optional.of(reference);
        }

    }

    public enum ReferenceType {

        AMBIGUOUS,
        VARIABLE,
        FUNCTION;

        public static ReferenceType getType(BulletLangParser.ReferenceContext ctx) {
            if (ctx.instType() != null) return VARIABLE;
            if (ctx.arguments() != null || ctx.LEFT_PAR() != null) return FUNCTION;
            return AMBIGUOUS;
        }

    }

}
