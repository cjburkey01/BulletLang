package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ATypeDec extends ABase {
    
    public final String identifier;
    
    public ATypeDec(String identifier, BulletParser.TypeDecContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
    }
    
}
