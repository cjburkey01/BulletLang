package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.classdef.BClass;
import com.cjburkey.bullet.obj.scope.BScope;
import com.cjburkey.bullet.obj.scope.IBScopeContainer;
import com.cjburkey.bullet.visitor.struct.Content;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/11
 */
public class BNamespace extends BBase implements IBScopeContainer {
    
    public final String name;
    public final List<BFunction> functions = new ArrayList<>();
    public final List<BClass> classes = new ArrayList<>();
    private final BScope scope = new BScope();
    
    public BNamespace(String name, List<Content> content, BulletParser.NamespaceContext ctx) {
        this(name, content, (ParserRuleContext) ctx);
    }
    
    BNamespace(String name, List<Content> contents, ParserRuleContext ctx) {
        super(ctx);
        
        this.name = name;
        for (Content content : contents) {
            if (content.classDec != null) {
                content.classDec.setParent(this);
                content.classDec.setNamespace(this);
                this.classes.add(content.classDec);
            } else if (content.function != null) {
                content.function.setParent(this);
                content.function.setNamespace(this);
                this.functions.add(content.function);
            }
        }
    }
    
    public <T extends IBBase> void load(List<T> dest, List<T> origin) {
        for (T t : origin) {
            dest.add(t);
            t.setParent(this);
            t.setNamespace(this);
        }
    }
    
    public String toString() {
        return String.format("Namespace [%s] has functions: %s and classes: %s", name, Arrays.toString(functions.toArray(new BFunction[0])), Arrays.toString(classes.toArray(new BClass[0])));
    }
    
    public BScope getScope() {
        return scope;
    }
    
}
