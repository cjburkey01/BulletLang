package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.scope.BScope;
import com.cjburkey.bullet.obj.scope.IBScopeContainer;
import com.cjburkey.bullet.visitor.struct.ProgramIn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BProgram extends BNamespace implements IBScopeContainer {
    
    public final List<String> requirements = new ArrayList<>();
    public final List<BNamespace> namespaces = new ArrayList<>();
    public final BScope scope = new BScope();
    
    public BProgram(List<String> requirements, ProgramIn content, BulletParser.ProgramContext ctx) {
        super("", content.contents, ctx);
        
        this.requirements.addAll(requirements);
        load(this.scope.statements, content.statements);
        load(this.namespaces, content.namespaces);
    }
    
    public <T extends BBase> void load(List<T> dest, List<T> origin) {
        for (T t : origin) {
            dest.add(t);
            t.setParent(this);
            t.setNamespace(this);
        }
    }
    
    public BScope getScope() {
        return scope;
    }
    
    public String toString() {
        return String.format("Module requires files: %s and has %s namespaces", Arrays.toString(requirements.toArray(new String[0])), namespaces.size());
    }
    
}
