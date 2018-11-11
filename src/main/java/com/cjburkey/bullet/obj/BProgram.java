package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.classdef.BClass;
import com.cjburkey.bullet.obj.scope.BScope;
import com.cjburkey.bullet.obj.scope.IBScopeContainer;
import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BProgram extends BBase implements IBScopeContainer {
    
    public final List<String> requirements = new ArrayList<>();
    public final List<BFunction> functions = new ArrayList<>();
    public final List<BClass> classes = new ArrayList<>();
    public final BScope scope = new BScope();
    
    public BProgram(List<String> requirements, List<BFunction> functions, List<BStatement> statements, List<BClass> classes, BulletParser.ProgramContext ctx) {
        super(ctx);
        
        this.requirements.addAll(requirements);
        this.functions.addAll(functions);
        this.scope.statements.addAll(statements);
        this.classes.addAll(classes);
    }
    
    public BScope getScope() {
        return scope;
    }
    
    public String toString() {
        return String.format("Module requires files: %s", Arrays.toString(requirements.toArray(new String[0])));
    }
    
}
