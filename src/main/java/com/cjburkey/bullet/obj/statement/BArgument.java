package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BBase;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BArgument extends BBase {
    
    public final String name;
    public final String type;
    
    public BArgument(BulletParser.ArgumentContext ctx) {
        super(ctx);
        
        name = ctx.IDENTIFIER(0).getText();
        type = ctx.IDENTIFIER().size() > 1 ? ctx.IDENTIFIER(1).getText() : null;
    }
    
    public String toString() {
        return name + " as " + (type == null ? '?' : type);
    }
    
}
