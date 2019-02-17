package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.ErrorHandler;
import com.cjburkey.bullet.antlr.BulletLangBaseVisitor;
import com.cjburkey.bullet.parser.component.Scope;
import java.util.Optional;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public abstract class BaseV<T> extends BulletLangBaseVisitor<Optional<T>> {

    public final Scope scope;

    public BaseV(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Optional<T> visit(ParseTree tree) {
        if (ErrorHandler.hasErrored() || tree == null) return Optional.empty();

        // This could be null because it might be unimplemented
        Optional<T> output = super.visit(tree);
        //noinspection OptionalAssignedToNull
        if (output == null) return Optional.empty();

        return output;
    }

}
