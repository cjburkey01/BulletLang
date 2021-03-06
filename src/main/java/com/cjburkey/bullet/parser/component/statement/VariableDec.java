package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.InstanceType;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.TypeDec;
import com.cjburkey.bullet.parser.component.classes.ClassInner;
import com.cjburkey.bullet.parser.component.expression.Expression;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class VariableDec extends ClassInner {

    private static final String ERROR_TYPE_NOT_RESOLVED = "Failed to resolve type for variable \"%s\" declaration based on value";
    private static final String ERROR_NO_TYPE_NO_VALUE = "Variable \"%s\" requires a type declaration";

    private InstanceType level;
    public String name;
    public TypeDec type;
    private Expression value;

    public VariableDec(ParserRuleContext ctx, InstanceType level, String name, TypeDec type, Expression value) {
        super(ctx);
        this.level = level;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Declare %s variable {%s} of type {%s} with value {%s}", level, name, type, value);
    }

    @Override
    public void doResolve(ObjectOpenHashSet<Base> exclude) {
        if (type != null && !exclude.contains(type)) type.resolve(this, exclude);
        if (value != null && !exclude.contains(value)) value.resolve(this, exclude);

        if (type == null) {
            if (value == null) {
                BulletError.queueError(ctx, ERROR_NO_TYPE_NO_VALUE, name);
            } else {
                type = new TypeDec(null, value.outputType);
                type.resolve(this, exclude);
            }
        }
        if (type != null && type.type == null) {
            BulletError.queueError(ctx, ERROR_TYPE_NOT_RESOLVED, name);
        }
    }

    public static final class Visitor extends BaseV<VariableDec> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<VariableDec> visitVariableDec(BulletLangParser.VariableDecContext ctx) {
            TypeDec typeDec = new TypeDec.Visitor(scope)
                    .visit(ctx.typeDec())
                    .orElse(null);
            Expression expression = new Expression.Visitor(scope)
                    .visit(ctx.expression())
                    .orElse(null);

            VariableDec variableDec = new VariableDec(ctx, InstanceType.get(ctx.instType()), ctx.IDENTIFIER().getText(), typeDec, expression);
            variableDec.parentScope = scope;
            scope.addVariable(variableDec);
            return Optional.of(variableDec);
        }

    }

}
