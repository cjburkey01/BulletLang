package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.visitor.SmartStringInn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BExpression extends BBase {
    
    public boolean isBool = false;
    public boolean isInt = false;
    public boolean isFloat = false;
    public boolean isString = false;
    public boolean isStringInter = false;
    public boolean isReference = false;
    public boolean isFunctionReference = false;
    
    public boolean boolVal = false;
    public int intVal = Integer.MIN_VALUE;
    public float floatVal = Float.NEGATIVE_INFINITY;
    public String stringVal = null;
    public String referenceVal = null;
    public final List<BExpression> arguments = new ArrayList<>();
    
    public BExpression(boolean boolVal, BulletParser.BooleanContext ctx) {
        super(ctx);
        
        this.boolVal = boolVal;
        this.isBool = true;
    }
    
    public BExpression(int intVal, BulletParser.IntegerContext ctx) {
        super(ctx);
        
        this.intVal = intVal;
        this.isInt = true;
    }
    
    public BExpression(float floatVal, BulletParser.FloatContext ctx) {
        super(ctx);
        
        this.floatVal = floatVal;
        this.isFloat = true;
    }
    
    public BExpression(String stringVal, boolean isStringInter, BulletParser.StringContext ctx) {
        super(ctx);
        
        this.stringVal = stringVal.substring(isStringInter ? 2 : 1, stringVal.length() - 1);
        this.isString = true;
        this.isStringInter = isStringInter;
    }
    
    public BExpression(String stringVal, BulletParser.LiteralStringContext ctx) {
        super(ctx);
        
        this.stringVal = stringVal.substring(3, stringVal.length() - 3);
        this.isString = true;
    }
    
    public BExpression(String referenceVal, BulletParser.ReferenceContext ctx) {
        super(ctx);
        
        this.referenceVal = referenceVal;
        this.isReference = true;
    }
    
    public String toString() {
        if (isBool) {
            return "Boolean: " + boolVal;
        }
        if (isInt) {
            return "Integer: " + intVal;
        }
        if (isFloat) {
            return "Float: " + floatVal;
        }
        if (isString && isStringInter) {
            return "Interpolated string: \"" + stringVal.replaceAll("\r?\n\r?", "\\\\n") + "\"";
        }
        if (isString) {
            return "String: \"" + stringVal.replaceAll("\r?\n\r?", "\\\\n") + "\"";
        }
        if (isReference && isFunctionReference) {
            return "Function reference: \"" + referenceVal + "\" with args: " + Arrays.toString(arguments.toArray(new BExpression[0]));
        }
        if (isReference) {
            return "Var/Func reference: " + referenceVal;
        }
        return "Expression";
    }
    
}
