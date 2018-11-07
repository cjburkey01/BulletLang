package com.cjburkey.bullet.obj.classdef;

/**
 * Created by CJ Burkey on 2018/11/07
 */
public enum BVariableType {
    
    STANDARD(0),
    LOCAL(1),
    STATIC(2);
    
    public final int count;
    
    BVariableType(int count) {
        this.count = count;
    }
    
    public static BVariableType get(int count) {
        for (BVariableType type : values()) {
            if (type.count == count) {
                return type;
            }
        }
        return null;
    }
    
}
