package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.classdef.BVisibility;
import com.cjburkey.bullet.obj.classdef.IBClassMember;
import com.cjburkey.bullet.obj.scope.BScope;
import com.cjburkey.bullet.obj.scope.IBScopeContainer;
import com.cjburkey.bullet.obj.statement.BArgument;
import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BFunction extends BBase implements IBScopeContainer, IBClassMember, IBAttribContainer {
    
    public final BAttribs attribs = new BAttribs();
    public final String name;
    public final String type;
    public final List<BArgument> arguments = new ArrayList<>();
    public final BScope scope = new BScope();
    
    public BFunction(List<String> attribs, String name, String type, List<BArgument> arguments, List<BStatement> statements, BulletParser.FunctionContext ctx) {
        super(ctx);
        
        this.attribs.attribs.addAll(attribs);
        this.name = name;
        this.type = type;
        this.arguments.addAll(arguments);
        scope.statements.addAll(statements);
    }
    
    public BScope getScope() {
        return scope;
    }
    
    public BVisibility getVisibility() {
        return attribs.getVisibility();
    }
    
    public String getName() {
        return name;
    }
    
    public String toString() {
        return String.format("Function [%s] (%s) (Arguments (%s): %s) returns [%s] and runs %s", name, attribs, arguments.size(), Arrays.toString(arguments.toArray(new BArgument[0])), type == null ? '?' : type, scope);
    }
    
    public BAttribs getAttribs() {
        return attribs;
    }
    
}
