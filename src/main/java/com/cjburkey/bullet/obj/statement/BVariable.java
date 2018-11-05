package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.classdef.BVisibility;
import com.cjburkey.bullet.obj.classdef.IBClassMember;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BVariable extends BStatement implements IBClassMember {
    
    public final String name;
    public final String type;
    public final BVisibility visibility;
    public BExpression value;
    
    public BVariable(String name, String type, BVisibility visibility, BExpression value, BulletParser.VariableDefContext ctx) {
        super(ctx);
        
        this.name = name;
        this.type = type;
        this.visibility = visibility;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public BVisibility getVisibility() {
        return visibility;
    }
    
    public String toString() {
        return String.format("Variable \"%s\" of type \"%s\" = %s", name, type == null ? '?' : type, value == null ? "Null" : value);
    }
    
}
