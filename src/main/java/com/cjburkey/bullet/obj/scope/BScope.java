package com.cjburkey.bullet.obj.scope;

import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("unused")
public final class BScope {
    
    public final List<BStatement> statements = new ArrayList<>();
    
    public String toString() {
        return Arrays.toString(statements.toArray(new BStatement[0]));
    }
    
}
