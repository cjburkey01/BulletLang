package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import java.util.Optional;

import static com.cjburkey.bullet.parser.ABase.*; 

/**
 * Created by CJ Burkey on 2018/11/19
 */
public enum AOperator {
    
    NOT("!"),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    NOT_EQUAL("!="),
    EQUAL("=="),
    LESS_EQUAL("<="),
    GREAT_EQUAL(">="),
    
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    POWER("**"),
    ROOT("//"),
    
    BIT_AND("&"),
    BIT_OR("|"),
    BIT_XOR("^"),
    BIT_NOT("~"),
    BIT_RIGHT(">>"),
    BIT_LEFT("<<"),
    
    ;
    
    public final String token;
    
    AOperator(String token) {
        this.token = token;
    }
    
    public String debug(int indent) {
        return getIndent(indent) + "Operator:\n" + getIndent(indent + indent()) + this + '\n';
    }
    
    public static Optional<AOperator> from(BulletParser.UnaryOpContext ctx) {
        if (ctx == null) {
            return Optional.empty();
        }
        if (ctx.BIT_NOT() != null) {
            return Optional.of(BIT_NOT);
        } else if (ctx.NOT() != null) {
            return Optional.of(NOT);
        } else if (ctx.POW() != null) {
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
        // Bitwise
        if (ctx.BIT_AND() != null) {
            return Optional.of(BIT_AND);
        } else if (ctx.BIT_OR() != null) {
            return Optional.of(BIT_OR);
        } else if (ctx.BIT_XOR() != null) {
            return Optional.of(BIT_XOR);
        } else if (ctx.BIT_RIGHT() != null) {
            return Optional.of(BIT_RIGHT);
        } else if (ctx.BIT_LEFT() != null) {
            return Optional.of(BIT_LEFT);
        } else
        // Equality
        if (ctx.LESS() != null) {
            return Optional.of(LESS_THAN);
        } else if (ctx.GREAT() != null) {
            return Optional.of(GREATER_THAN);
        } else if (ctx.LESS_EQ() != null) {
            return Optional.of(LESS_EQUAL);
        } else if (ctx.GREAT_EQ() != null) {
            return Optional.of(GREAT_EQUAL);
        } else if (ctx.EQUAL() != null) {
            return Optional.of(EQUAL);
        } else if (ctx.NOT_EQ() != null) {
            return Optional.of(NOT_EQUAL);
        } else
        // Mathematical
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
        } else if (ctx.GREAT() != null) {
            return Optional.of(GREATER_THAN);
        } else if (ctx.LESS() != null) {
            return Optional.of(LESS_THAN);
        }
        return Optional.empty();
    }
    
    public static Optional<AOperator> from(BulletParser.OpContext ctx) {
        if (ctx == null) {
            return Optional.empty();
        }
        // Bitwise
        if (ctx.BIT_AND() != null) {
            return Optional.of(BIT_AND);
        } else if (ctx.BIT_OR() != null) {
            return Optional.of(BIT_OR);
        } else if (ctx.BIT_XOR() != null) {
            return Optional.of(BIT_XOR);
        } else if (ctx.BIT_RIGHT() != null) {
            return Optional.of(BIT_RIGHT);
        } else if (ctx.BIT_LEFT() != null) {
            return Optional.of(BIT_LEFT);
        } else
        // Equality
        if (ctx.LESS() != null) {
            return Optional.of(LESS_THAN);
        } else if (ctx.GREAT() != null) {
            return Optional.of(GREATER_THAN);
        } else if (ctx.LESS_EQ() != null) {
            return Optional.of(LESS_EQUAL);
        } else if (ctx.GREAT_EQ() != null) {
            return Optional.of(GREAT_EQUAL);
        } else if (ctx.EQUAL() != null) {
            return Optional.of(EQUAL);
        } else if (ctx.NOT_EQ() != null) {
            return Optional.of(NOT_EQUAL);
        } else
        // Mathematical
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
        } else if (ctx.GREAT() != null) {
            return Optional.of(GREATER_THAN);
        } else if (ctx.LESS() != null) {
            return Optional.of(LESS_THAN);
        }
        return Optional.empty();
    }
    
}
