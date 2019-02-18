package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletLangParser;

/**
 * Created by CJ Burkey on 2019/02/17
 */
public enum InstanceType {

    DEFAULT(0),
    INSTANCE(1),
    STATIC(2),

    ;

    public final int level;

    InstanceType(int level) {
        this.level = level;
    }

    public static InstanceType get(BulletLangParser.InstTypeContext ctx) {
        if (ctx != null) {
            if (ctx.LOCAL() != null) return INSTANCE;
            if (ctx.STATIC() != null) return STATIC;
        }
        return DEFAULT;
    }

}
