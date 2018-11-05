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
public class BFunction extends BBase implements IBScopeContainer, IBClassMember {
    
    public final String name;
    public final String type;
    public final BVisibility visibility;
    public final List<BArgument> arguments = new ArrayList<>();
    public final BScope scope = new BScope();
    
    public BFunction(String name, String type, BVisibility visibility, List<BArgument> arguments, List<BStatement> statements, BulletParser.FunctionContext ctx) {
        super(ctx);
        
        this.name = name;
        this.type = type;
        this.visibility = visibility;
        this.arguments.addAll(arguments);
        scope.statements.addAll(statements);
    }
    
    public BScope getScope() {
        return scope;
    }
    
    public String getName() {
        return name;
    }
    
    public BVisibility getVisibility() {
        return visibility;
    }
    
    public String toString() {
        return String.format("Function \"%s\" (Arguments: %s) returns \"%s\" and runs %s", name, Arrays.toString(arguments.toArray(new BArgument[0])), type == null ? '?' : type, Arrays.toString(scope.statements.toArray(new BStatement[0])));
    }
}
