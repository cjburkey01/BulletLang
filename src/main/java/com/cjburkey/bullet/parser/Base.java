package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.parser.component.Scope;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Objects;
import java.util.UUID;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public abstract class Base {

    public Scope parentScope;
    public ParserRuleContext ctx;
    private final UUID uuid = UUID.randomUUID();
    private boolean resolved = false;

    public Base(ParserRuleContext ctx) {
        this.ctx = ctx;
    }

    public final void resolve(Base self, ObjectOpenHashSet<Base> exclude) {
        if (!resolved) resolveRaw(self, this, exclude);
    }

    private static void resolveRaw(Base self, Base resolvee, ObjectOpenHashSet<Base> exclude) {
        resolvee.resolved = true;
        final var set = new ObjectOpenHashSet<>(exclude);
        if (self != null) set.add(self);
        resolvee.doResolve(set);
    }

    protected abstract void doResolve(ObjectOpenHashSet<Base> exclude);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Base base = (Base) o;
        return uuid.equals(base.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

}
