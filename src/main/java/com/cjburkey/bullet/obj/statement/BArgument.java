package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BBase;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BArgument extends BBase {
    
    public final String name;
    public final String type;
    
    public BArgument(String name, String type, BulletParser.ArgumentContext ctx) {
        super(ctx);
        
        this.name = name;
        this.type = type;
    }
    
    public String toString() {
        return name + " as " + (type == null ? '?' : type);
    }
    
}
