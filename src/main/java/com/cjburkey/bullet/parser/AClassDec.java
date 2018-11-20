package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class AClassDec extends ABase {
    
    public AClassDec(BulletParser.ClassDefContext ctx) {
        super(ctx);
    }
    
}
