package com.cjburkey.bullet.parser;

import java.util.Objects;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public abstract class ABase {
    
    private static int nextId;
    
    public final int id;
    public final ParserRuleContext ctx;
    private ABase parent;
    public final StackTraceElement[] stackTrace;
    
    public ABase(ParserRuleContext ctx) {
        this.id = nextId ++;
        this.ctx = ctx;
        stackTrace = new Exception().getStackTrace();
    }
    
    public ABase getParent() {
        return parent;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ABase aBase = (ABase) o;
        return id == aBase.id && ctx.equals(aBase.ctx);
    }
    
    public int hashCode() {
        return Objects.hash(id, ctx);
    }
    
}
