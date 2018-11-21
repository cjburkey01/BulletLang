package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public abstract class ABase {
    
    public final ParserRuleContext ctx;
    
    public ABase(ParserRuleContext ctx) {
        this.ctx = ctx;
    }
    
    public final String debug(int indent) {
        String output = getFormattedDebug(indent);
        if (!output.endsWith("\n")) {
            output += '\n';
        }
        return output;
    }
    
    public abstract String getFormattedDebug(int indent);
    public abstract ObjectArrayList<BulletVerifyError> verify();
    
    @SuppressWarnings("unchecked")
    protected static ObjectArrayList<BulletVerifyError> verifyLists(Collection<? extends ABase>... lists) {
        ObjectArrayList<BulletVerifyError> output = new ObjectArrayList<>();
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
