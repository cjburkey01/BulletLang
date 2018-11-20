package com.cjburkey.bullet.parser.requirement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ARequirement extends ABase {
    
    public final String string;
    
    public ARequirement(String string, BulletParser.RequirementContext ctx) {
        super(ctx);
        
        this.string = string;
    }
    
}
