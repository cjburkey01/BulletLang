package com.cjburkey.bullet.compiler.error;

import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/12
 */
public class ErrorDuplicateFunction extends ErrorCompile {
    
    public ErrorDuplicateFunction(String name, List<String> argumentTypes) {
        super(String.format("Duplicate function: [%s] with args: %s", name, Arrays.toString(argumentTypes.toArray(new String[0]))));
    }
    
}
