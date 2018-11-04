package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BVariable extends BStatement {
    
    public final String name;
    public final String type;
    public BExpression value;
    
    public BVariable(String name, String type, BExpression value, BulletParser.VariableDefContext ctx) {
        super(ctx);
        
        this.name = name;
        this.type = type;
        this.value = value;
    }
    
    public String toString() {
        return String.format("Define variable \"%s\" of type \"%s\" = %s", name, type == null ? '?' : type, value == null ? "Null" : value);
    }
    
}
