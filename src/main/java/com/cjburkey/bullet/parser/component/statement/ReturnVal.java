package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.TypeDec;
import com.cjburkey.bullet.parser.component.expression.Expression;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class ReturnVal extends Statement {

    private static final String ERROR_RETURN_TYPE_MISMATCH = "The return type of the function \"%s\" of type %s differs from the return statement of type %s";
    private static final String ERROR_NO_FUNCTION = "No function found from which to return";

    private Expression value;

    private ReturnVal(ParserRuleContext ctx, Expression value) {
        super(ctx);
        this.value = value;
    }

    @Override
    public String toString() {
        return "Return: {" + value + "}";
    }

    @Override
    public void resolveTypes() {
        value.resolveTypes();

        IScopeContainer parent = parentScope.parentContainer;
        boolean foundParentFunction = false;
        do {
            if (parent instanceof FunctionDec) {
                FunctionDec p = (FunctionDec) parent;
                if (p.type == null) {
                    p.type = new TypeDec(ctx, value.outputType);
                } else {
                    if (p.type.type == null) {
                        p.type.type = value.outputType;
                    } else {
                        BulletError.queueError(ctx, ERROR_RETURN_TYPE_MISMATCH, p.name, p.type, value.outputType);
                    }
                }

                foundParentFunction = true;
                break;
            }

            parent = parent.getScope().parentContainer;
        } while (parent != null);

        if (!foundParentFunction) {
            BulletError.queueError(ctx, ERROR_NO_FUNCTION);
        }
    }

    @Override
    public void resolveReferences() {
        value.resolveReferences();
    }

    public static final class Visitor extends BaseV<ReturnVal> {

        public Visitor(Scope parentScope) {
            super(parentScope);
        }

        @Override
        public Optional<ReturnVal> visitReturnVal(BulletLangParser.ReturnValContext ctx) {
            ReturnVal returnVal = new ReturnVal(ctx, new Expression.Visitor(scope)
                    .visit(ctx.expression())
                    .orElse(null));
            returnVal.parentScope = scope;
            return Optional.of(returnVal);
        }

    }

}
