package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.IScopeContainer;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Program extends Base implements IScopeContainer {

    public final Scope scope;

    private Program(ParserRuleContext ctx, Scope scope) {
        super(ctx);
        this.scope = scope;
    }

    @Override
    public void doResolve(ObjectOpenHashSet<Base> exclude) {
        scope.resolve(this, exclude);
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return String.format("Program scope: {%s}", scope);
    }

    public static final class Visitor extends BaseV<Program> {

        public Visitor() {
            super(null);
        }

        @Override
        public Optional<Program> visitProgram(BulletLangParser.ProgramContext ctx) {
            Scope scope = new Scope.Visitor(null)
                    .visit(ctx.scope())
                    .orElseGet(() -> new Scope(null, null));
            Program program = new Program(ctx, scope);
            scope.parentContainer = program;
            return Optional.of(program);
        }

    }

}
