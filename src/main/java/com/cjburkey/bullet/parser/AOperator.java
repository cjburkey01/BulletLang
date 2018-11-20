package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
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
    
    public static Optional<AOperator> from(BulletParser.UnaryOpContext ctx) {
        if (ctx == null) {
            return Optional.empty();
        }
        if (ctx.POW() != null) {
            return Optional.of(POWER);
        } else if (ctx.ROOT() != null) {
            return Optional.of(ROOT);
        } else if (ctx.MINUS() != null) {
            return Optional.of(MINUS);
        }
        return Optional.empty();
    }
    
    public static Optional<AOperator> from(BulletParser.BinaryOpContext ctx) {
        if (ctx == null) {
            return Optional.empty();
        }
        if (ctx.POW() != null) {
            return Optional.of(POWER);
        } else if (ctx.ROOT() != null) {
            return Optional.of(ROOT);
        } else if (ctx.TIMES() != null) {
            return Optional.of(TIMES);
        } else if (ctx.DIV() != null) {
            return Optional.of(DIVIDE);
        } else if (ctx.PLUS() != null) {
            return Optional.of(PLUS);
        } else if (ctx.MINUS() != null) {
            return Optional.of(MINUS);
        }
        return Optional.empty();
    }
    
    public static Optional<AOperator> from(BulletParser.FunctionDecContext ctx) {
        if (ctx == null) {
            return Optional.empty();
        }
        if (ctx.POW() != null) {
            return Optional.of(POWER);
        } else if (ctx.ROOT() != null) {
            return Optional.of(ROOT);
        } else if (ctx.TIMES() != null) {
            return Optional.of(TIMES);
        } else if (ctx.DIV() != null) {
            return Optional.of(DIVIDE);
        } else if (ctx.PLUS() != null) {
            return Optional.of(PLUS);
        } else if (ctx.MINUS() != null) {
            return Optional.of(MINUS);
        }
        return Optional.empty();
    }
    
}
