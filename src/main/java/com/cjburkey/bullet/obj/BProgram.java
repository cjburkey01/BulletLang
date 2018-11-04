package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.scope.BScope;
import com.cjburkey.bullet.obj.scope.IBScopeContainer;
import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BProgram extends BBase implements IBScopeContainer {
    
    public final String module;
    public final List<String> requirements = new ArrayList<>();
    public final List<BFunction> functions = new ArrayList<>();
    public final BScope scope = new BScope();
    
    public BProgram(String module, List<String> requirements, List<BFunction> functions, List<BStatement> statements, BulletParser.ProgramContext ctx) {
        super(ctx);
        
        this.module = module;
        this.requirements.addAll(requirements);
        this.functions.addAll(functions);
        this.scope.statements.addAll(statements);
    }
    
    public BScope getScope() {
        return scope;
    }
    
    public String toString() {
        return String.format("\"%s\" requires files: %s", module, Arrays.toString(requirements.toArray(new String[0])));
    }
    
}
