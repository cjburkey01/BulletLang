package com.cjburkey.bullet.obj.scope;

import com.cjburkey.bullet.obj.IBBase;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("unused")
public interface IBScopeContainer extends IBBase {
    
    BScope getScope();
    
    default <T extends IBBase> void load(List<T> dest, List<T> origin) {
        for (T t : origin) {
            dest.add(t);
            t.setParent(this);
        }
    }
    
}
