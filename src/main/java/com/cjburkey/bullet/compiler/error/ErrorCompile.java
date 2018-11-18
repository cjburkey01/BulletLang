package com.cjburkey.bullet.compiler.error;

/**
 * Created by CJ Burkey on 2018/11/12
 */
@SuppressWarnings("WeakerAccess")
public class ErrorCompile {
    
    public final String message;
    
    public ErrorCompile(String message) {
        this.message = message;
    }
    
    public String toString() {
        return String.format("Compiler error: %s", message);
    }
    
}
