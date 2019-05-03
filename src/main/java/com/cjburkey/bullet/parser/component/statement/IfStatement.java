package com.cjburkey.bullet.parser.component.statement;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.Scope;
import com.cjburkey.bullet.parser.component.expression.Expression;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class IfStatement extends BranchStatement {

    private final ObjectArrayList<BranchStatement> elseBranches = new ObjectArrayList<>();

    private IfStatement(ParserRuleContext ctx, Expression condition, Scope scope, ObjectArrayList<BranchStatement> elseBranches) {
        super(ctx, condition, scope);

        this.elseBranches.addAll(elseBranches);
    }

    @Override
    public String toString() {
        return String.format("If statement {%s} with else branches {%s}",
                super.toString(),
                elseBranches.toString());
    }

    @Override
    protected void doResolve(ObjectOpenHashSet<Base> exclude) {
        super.doResolve(exclude);

        elseBranches.forEach(branch -> {
            if (!exclude.contains(branch)) branch.resolve(this, exclude);
        });
    }

    public static final class Visitor extends BaseV<IfStatement> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<IfStatement> visitIfRaw(BulletLangParser.IfRawContext ctx) {
            final var condition = new Expression.Visitor(this.scope)
                    .visit(ctx.expression())
                    .orElse(null);
            final var scope = new Scope.Visitor(this.scope)
                    .visit(ctx.scope())
                    .orElse(new Scope(ctx, null));
            final var elseBranches = ctx.elseRaw()
                    .stream()
                    .map(elseRaw -> new BranchStatement.Visitor(this.scope).visit(elseRaw))
                    .map(opt -> opt.orElse(null))
                    .collect(Collectors.toCollection(ObjectArrayList::new));
            final var ifStatement = new IfStatement(ctx, condition, scope, elseBranches);
            scope.parentContainer = ifStatement;
            return Optional.of(ifStatement);
        }

        @Override
        public Optional<IfStatement> visitIfStatement(BulletLangParser.IfStatementContext ctx) {
            return visitIfRaw(ctx.ifRaw());
        }

    }

}
