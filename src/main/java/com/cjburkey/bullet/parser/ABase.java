package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public abstract class ABase implements IABase {
    
    private static int nextId;
    
    public final int id;
    public final ParserRuleContext ctx;
    private IScopeContainer scope;
    private ABase parent;
    
    public ABase(ParserRuleContext ctx) {
        this.id = nextId ++;
        this.ctx = ctx;
    }
    
    public void setScopeParent(IScopeContainer scope, ABase parent) {
        this.scope = scope;
        this.parent = parent;
        settleChildren();
    }
    
    public IScopeContainer getScope() {
        return scope;
    }
    
    public ABase getParent() {
        return parent;
    }
    
    public final String debug(int indent) {
        String output = getFormattedDebug(indent);
        if (!output.endsWith("\n")) {
            output += '\n';
        }
        return output;
    }
    
    @SuppressWarnings("unchecked")
    protected static ObjectArrayList<BulletError> verifyLists(Collection<? extends ABase>... lists) {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        for (Collection<? extends ABase> list : lists) list.forEach(element -> output.addAll(element.verify()));
        return output;
    }
    
    public static String getIndent(int amt) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < amt; i ++) output.append(i % indent() == 0 ? '|' : ' ');
        return output.toString();
    }
    
    public static int indent() {
        return 2;
    }
    
}