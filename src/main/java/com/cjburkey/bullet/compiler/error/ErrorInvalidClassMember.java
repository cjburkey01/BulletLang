package com.cjburkey.bullet.compiler.error;

import com.cjburkey.bullet.obj.classdef.IBClassMember;

/**
 * Created by CJ Burkey on 2018/11/12
 */
public class ErrorInvalidClassMember extends ErrorCompile {
    
    public ErrorInvalidClassMember(IBClassMember member) {
        super(String.format("Invalid class member: [%s]", member));
    }
    
}
