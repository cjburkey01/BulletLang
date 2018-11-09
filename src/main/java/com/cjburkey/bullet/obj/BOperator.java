package com.cjburkey.bullet.obj;

/**
 * Created by CJ Burkey on 2018/11/07
 */
public enum BOperator {
    
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIV("/"),
    POW("**"),
    ROOT("//");
    
    final String op;
    
    BOperator(String op) {
        this.op = op;
    }
    
}
