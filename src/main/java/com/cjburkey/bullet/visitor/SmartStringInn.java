package com.cjburkey.bullet.visitor;

import com.cjburkey.bullet.obj.BExpression;

/**
 * Created by CJ Burkey on 2018/11/04
 */
public class SmartStringInn {
    
    public final String string;
    public final BExpression expression;
    
    public SmartStringInn(String string, BExpression expression) {
        this.string = string;
        this.expression = expression;
    }
    
    public String toString() {
        if (string != null) {
            return string;
        }
        if (expression != null) {
            return expression.toString();
        }
        return "SmartStringContent";
    }
    
}
