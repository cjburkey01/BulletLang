package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.expression.Expression;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
@SuppressWarnings("WeakerAccess")
public class BranchStatement extends Statement implements IScopeContainer {

    public Expression condition;
    public Scope scope;

    BranchStatement(ParserRuleContext ctx, Expression condition, Scope scope) {
        super(ctx);

        this.condition = condition;
        this.scope = scope;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return String.format("Branch with condition {%s} and scope {%s}", condition, scope);
    }

    @Override
    protected void doResolve(ObjectOpenHashSet<Base> exclude) {
        if (condition != null && !exclude.contains(condition)) condition.resolve(this, exclude);
        if (!exclude.contains(scope)) scope.resolve(this, exclude);

        if (condition != null && !condition.outputType.toString().equals("Boolean")) {
            BulletError.queueError(ctx, "Invalid type for conditional statement \"%s\"", condition.outputType);
        }
    }

    static final class Visitor extends BaseV<BranchStatement> {

        Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<BranchStatement> visitElseRaw(BulletLangParser.ElseRawContext ctx) {
            final var condition = new Expression.Visitor(this.scope)
                    .visit(ctx.expression())
                    .orElse(null);
            final var scope = new Scope.Visitor(this.scope)
                    .visit(ctx.scope())
                    .orElse(new Scope(ctx, null));
            final var branch = new BranchStatement(ctx, condition, scope);
            scope.parentContainer = branch;
            return Optional.of(branch);
        }

    }

}
