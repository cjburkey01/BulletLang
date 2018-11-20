package com.cjburkey.bullet.parser;

import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public enum AOperator {
    
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    POWER("**"),
    ROOT("//"),
    
    ;
    
    public final String token;
    
    AOperator(String token) {
        this.token = token;
    }
    
    public static Optional<AOperator> fromToken(String token) {
        for (AOperator operator : values()) {
            if (operator.token.equals(token)) {
                return Optional.of(operator);
            }
        }
        return Optional.empty();
    }
    
}
