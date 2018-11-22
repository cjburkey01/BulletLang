package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AFloat extends AExpression {
    
    public final double floating;
    public final boolean valid;
    
    public AFloat(String floating, BulletParser.FloatContext ctx) {
        super(ctx);
        
        double value = 0.0d;
        boolean isValid = false;
        try {
            value = Double.parseDouble(floating);
            isValid = true;
        } catch (Exception ignored) {
        }
        this.floating = value;
        this.valid = isValid;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "Float:\n" + getIndent(indent + indent()) + floating + '\n';
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return new ObjectArrayList<>();
    }
    
    public void settleChildren() {
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        if (!valid) output.add(new BulletError("Invalid floating point number", ctx));
        return output;
    }
    
}