package com.cjburkey.bullet.parser.requirement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings("WeakerAccess")
public class BRequirement extends ABase {
    
    public final String location;
    
    public BRequirement(String location, BulletParser.ReferenceContext ctx) {
        super(ctx);
        
        this.location = location;
    }
    
}
