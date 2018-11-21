package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AInteger extends AExpression {
    
    public final long integer;
    public final boolean valid;
    
    public AInteger(String integer, BulletParser.IntegerContext ctx) {
        super(ctx);
        
        long value = 0L;
        boolean isValid = false;
        try {
            value = Long.parseLong(integer);
            isValid = true;
        } catch (Exception ignored) {
        }
        this.integer = value;
        this.valid = isValid;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "Integer:\n" + getIndent(indent + indent()) + integer + '\n';
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        ObjectArrayList<BulletVerifyError> output = new ObjectArrayList<>();
        if (!valid) output.add(new BulletVerifyError("Invalid integer", ctx));
        return output;
    }
    
}
