package com.cjburkey.bullet.visitor.struct;

import com.cjburkey.bullet.obj.BFunction;
import com.cjburkey.bullet.obj.statement.BVariable;

/**
 * Created by CJ Burkey on 2018/11/05
 */
@SuppressWarnings("WeakerAccess")
public class ClassMember {
    
    public final BVariable variableDeclaration;
    public final BFunction methodDeclaration;
    
    public ClassMember(BVariable variableDeclaration, BFunction methodDeclaration) {
        this.variableDeclaration = variableDeclaration;
        this.methodDeclaration = methodDeclaration;
    }
    
}
