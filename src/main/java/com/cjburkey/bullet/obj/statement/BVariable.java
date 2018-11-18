package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.classdef.BVariableType;
import com.cjburkey.bullet.obj.classdef.BVisibility;
import com.cjburkey.bullet.obj.classdef.IBClassMember;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BVariable extends BStatement implements IBClassMember {
    
    public final boolean declaration;
    public final String name;
    public final String type;
    public final BVariableType declaredType;
    public BExpression value;
    
    public BVariable(boolean declaration, String name, String type, BVariableType declaredType, BExpression value, BulletParser.VariableDefContext ctx) {
        super(ctx);
        
        this.declaration = declaration;
        this.name = name;
        this.type = type;
        this.declaredType = declaredType;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public BVisibility getVisibility() {
        return BVisibility.PRIVATE;
    }
    
    public String toString() {
        return String.format("Variable%s [%s] is [%s] of declaration type [%s] set to [%s]", declaration ? " declaration" : "", name, declaredType, type == null ? '?' : type, value == null ? "Null" : value);
    }
    
}
