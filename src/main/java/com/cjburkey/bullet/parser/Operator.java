package com.cjburkey.bullet.parser;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public enum Operator {

    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),

    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    EQ("=="),
    NEQ("!="),

    AND("&&"),
    OR("||"),

    ;

    public final String token;

    Operator(String token) {
        this.token = token;
    }

    public static Operator resolve(String token) {
        for (Operator operator : values()) {
            if (operator.token.equals(token)) return operator;
        }
        return null;
    }

}
