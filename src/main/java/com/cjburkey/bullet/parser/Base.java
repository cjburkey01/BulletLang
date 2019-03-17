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

    public Base(ParserRuleContext ctx) {
        this.ctx = ctx;
    }

    public abstract void resolve(ObjectOpenHashSet<Base> exclude);

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
