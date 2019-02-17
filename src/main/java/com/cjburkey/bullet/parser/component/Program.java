package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.IScopeContainer;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Program extends Base implements IScopeContainer {

    public final Scope scope;

    private Program(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public static final class Visitor extends BaseV<Program> {

        public Visitor() {
            super(null);
        }

        public Optional<Program> visitProgram(BulletLangParser.ProgramContext ctx) {
            Scope scope = new Scope.Visitor(null)
                    .visit(ctx.scope())
                    .orElseGet(Scope::new);
            Program program = new Program(scope);
            return Optional.of(program);
        }

    }

}
