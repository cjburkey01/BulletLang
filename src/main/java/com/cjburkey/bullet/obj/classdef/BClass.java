package com.cjburkey.bullet.obj.classdef;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/05
 */
@SuppressWarnings("WeakerAccess")
public class BClass extends BBase {
    
    public final String name;
    public final List<String> types = new ArrayList<>();
    public final BVisibility visibility;
    public final List<IBClassMember> members = new ArrayList<>();
    
    public BClass(String name, BVisibility visibility, List<String> types, List<IBClassMember> members, BulletParser.ClassDefContext ctx) {
        super(ctx);
        
        this.name = name;
        this.types.addAll(types);
        this.members.addAll(members);
        this.visibility = visibility;
    }
    
    public String toString() {
        return String.format("Class [%s] inherits from: %s and has members: %s", name, Arrays.toString(types.toArray(new String[0])), Arrays.toString(members.toArray(new IBClassMember[0])));
    }
    
}
