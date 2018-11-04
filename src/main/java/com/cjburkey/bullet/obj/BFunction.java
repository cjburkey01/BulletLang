package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
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
public class BFunction extends BBase implements IBScopeContainer {
    
    public final String name;
    public final String type;
    public final List<BArgument> arguments = new ArrayList<>();
    public final BScope scope = new BScope();
    
    public BFunction(String name, String type, List<BArgument> arguments, List<BStatement> statements, BulletParser.FunctionContext ctx) {
        super(ctx);
        
        this.name = name;
        this.type = type;
        this.arguments.addAll(arguments);
        scope.statements.addAll(statements);
    }
    
    public BScope getScope() {
        return scope;
    }
    
    public String toString() {
        return String.format("\"%s\" (Arguments: %s) returns \"%s\" and runs %s", name, Arrays.toString(arguments.toArray(new BArgument[0])), type == null ? '?' : type, Arrays.toString(scope.statements.toArray(new BStatement[0])));
    }
    
}
