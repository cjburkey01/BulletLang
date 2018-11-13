package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BAttribs;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.classdef.BVariableType;
import com.cjburkey.bullet.obj.classdef.BVisibility;
import com.cjburkey.bullet.obj.classdef.IBClassMember;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BVariable extends BStatement implements IBClassMember {
    
    public final String name;
    public final String type;
    public final BVariableType declarationType;
    public BExpression value;
    
    public BVariable(String name, String type, BVariableType declarationType, BExpression value, BulletParser.VariableDefContext ctx) {
        super(ctx);
        
        this.name = name;
        this.type = type;
        this.declarationType = declarationType;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public BVisibility getVisibility() {
        return BVisibility.PRIVATE;
    }
    
    public String toString() {
        return String.format("Variable [%s] is [%s] of declaration type [%s] set to [%s]", name, declarationType, type == null ? '?' : type, value == null ? "Null" : value);
    }
    
}
