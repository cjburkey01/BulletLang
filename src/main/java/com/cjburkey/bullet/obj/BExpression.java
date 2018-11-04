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
    
    public boolean isInt = false;
    public boolean isFloat = false;
    public boolean isStringLiteral;
    public boolean isReference = false;
    public boolean isFunctionReference;
    
    public int intVal = Integer.MIN_VALUE;
    public float floatVal = Float.NEGATIVE_INFINITY;
    public String stringLiteral = null;
    public String variableReference = null;
    public List<BExpression> arguments = new ArrayList<>();
    
    public BExpression(BulletParser.ExpressionContext ctx) {
        super(ctx);
        
        if (ctx instanceof BulletParser.IntegerContext) {
            intVal = Integer.parseInt(ctx.getText());
            isInt = true;
        }
        if (ctx instanceof BulletParser.FloatContext) {
            floatVal = Float.parseFloat(ctx.getText());
            isFloat = true;
        }
        if (ctx instanceof BulletParser.StringContext) {
            stringLiteral = ctx.getText();
            stringLiteral = stringLiteral.substring(1, stringLiteral.length() - 1);
            isStringLiteral = true;
        }
        if (ctx instanceof BulletParser.LiteralStringContext) {
            stringLiteral = ctx.getText();
            stringLiteral = stringLiteral.substring(3, stringLiteral.length() - 3);
            isStringLiteral = true;
        }
        if (ctx instanceof BulletParser.ReferenceContext) {
            BulletParser.ReferenceContext context = (BulletParser.ReferenceContext) ctx;
            variableReference = context.IDENTIFIER().getText();
            isReference = true;
            if (context.LP() != null && context.RP() != null || context.funcParams() != null) {
                BulletParser.FuncParamsContext funcParams = context.funcParams();
                while (funcParams != null) {
                    arguments.add(new BExpression(funcParams.expression()));
                    funcParams = funcParams.funcParams();
                }
                isFunctionReference = true;
            }
        }
    }
    
    public String toString() {
        if (isInt) {
            return "Integer: " + intVal;
        }
        if (isFloat) {
            return "Float: " + floatVal;
        }
        if (isStringLiteral) {
            return "String: \"" + stringLiteral.replaceAll("\r?\n\r?", "\\\\n") + "\"";
        }
        if (isReference && isFunctionReference) {
            return "Function reference: \"" + variableReference + "\" with args: " + Arrays.toString(arguments.toArray(new BExpression[0]));
        }
        if (isReference) {
            return "Var/Func reference: " + variableReference;
        }
        return "Expression";
    }
    
}
