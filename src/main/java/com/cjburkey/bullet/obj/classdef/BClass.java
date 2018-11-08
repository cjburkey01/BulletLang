package com.cjburkey.bullet.obj.classdef;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BAttribs;
import com.cjburkey.bullet.obj.BBase;
import com.cjburkey.bullet.obj.IBAttribContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/05
 */
@SuppressWarnings("WeakerAccess")
public class BClass extends BBase implements IBAttribContainer {
    
    public final BAttribs attribs = new BAttribs();
    public final String name;
    public final List<String> types = new ArrayList<>();
    public final boolean priv;
    public final List<IBClassMember> members = new ArrayList<>();
    
    public BClass(String name, boolean priv, List<String> types, List<IBClassMember> members, BulletParser.ClassDefContext ctx) {
        super(ctx);
        
        this.name = name;
        this.types.addAll(types);
        this.members.addAll(members);
        this.priv = priv;
    }
    
    public String toString() {
        return String.format("Class [%s] (%s) inherits from: %s and has members: %s", name, attribs, Arrays.toString(types.toArray(new String[0])), Arrays.toString(members.toArray(new IBClassMember[0])));
    }
    
    public BAttribs getAttribs() {
        return attribs;
    }
    
}
