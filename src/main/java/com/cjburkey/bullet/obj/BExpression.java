package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
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
    public boolean isVariableReference = false;
    public boolean isUnaryOp = false;
    public boolean isBinaryOp = false;
    
    public boolean boolVal = false;
    public int intVal = Integer.MIN_VALUE;
    public float floatVal = Float.NEGATIVE_INFINITY;
    public String stringVal = null;
    public String referenceVal = null;
    public final List<BExpression> arguments = new ArrayList<>();
    public Operator operator = null;
    public BExpression operandA = null;
    public BExpression operandB = null;
    
    public BExpression(boolean boolVal, BulletParser.BooleanContext ctx) {
        super(ctx);
        
        this.isBool = true;
        this.boolVal = boolVal;
    }
    
    public BExpression(int intVal, BulletParser.IntegerContext ctx) {
        super(ctx);
        
        this.isInt = true;
        this.intVal = intVal;
    }
    
    public BExpression(float floatVal, BulletParser.FloatContext ctx) {
        super(ctx);
        
        this.isFloat = true;
        this.floatVal = floatVal;
    }
    
    public BExpression(String stringVal, boolean isStringInter, BulletParser.StringContext ctx) {
        super(ctx);
        
        this.isString = true;
        this.isStringInter = isStringInter;
        this.stringVal = stringVal.substring(isStringInter ? 2 : 1, stringVal.length() - 1);
    }
    
    public BExpression(String stringVal, BulletParser.LiteralStringContext ctx) {
        super(ctx);
        
        this.isString = true;
        this.stringVal = stringVal.substring(3, stringVal.length() - 3);
    }
    
    public BExpression(String referenceVal, BulletParser.ReferenceContext ctx) {
        super(ctx);
        
        this.isReference = true;
        this.referenceVal = referenceVal;
    }
    
    public BExpression(Operator operator, BExpression operand, BulletParser.UnaryOpContext ctx) {
        super(ctx);
        
        this.isUnaryOp = true;
        this.operator = operator;
        operandA = operand;
    }
    
    public BExpression(Operator operator, BExpression operandA, BExpression operandB, BulletParser.BinaryOpContext ctx) {
        super(ctx);
        
        this.isBinaryOp = true;
        this.operator = operator;
        this.operandA = operandA;
        this.operandB = operandB;
    }
    
    public String cleanStringVal() {
        return stringVal.replaceAll("\r?\n\r?", "\\\\n");
    }
    
    public String toString() {
        if (isBool) {
            return String.format("Boolean: [%s]", boolVal);
        }
        if (isInt) {
            return String.format("Integer: [%s]", intVal);
        }
        if (isFloat) {
            return String.format("Float: [%s]", floatVal);
        }
        if (isString && isStringInter) {
            return String.format("Interpolated string: [\"%s\"]", cleanStringVal());
        }
        if (isString) {
            return String.format("String: [\"%s\"]", cleanStringVal());
        }
        if (isReference && isFunctionReference) {
            return String.format("Function reference: [%s] with args (%s): %s", referenceVal, arguments.size(), Arrays.toString(arguments.toArray(new BExpression[0])));
        }
        if (isReference && isVariableReference) {
            return String.format("Variable reference: [%s]", referenceVal);
        }
        if (isReference) {
            return String.format("Ambiguous reference: [%s]", referenceVal);
        }
        if (isUnaryOp) {
            return String.format("Unary operator: \"%s\" on operand: [%s]", operator, operandA);
        }
        if (isBinaryOp) {
            return String.format("Binary operator: \"%s\" on operands: [%s, %s]", operator, operandA, operandB);
        }
        return "INVALID_EXPRESSION";
    }
    
}
