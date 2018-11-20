package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;

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
    
}
