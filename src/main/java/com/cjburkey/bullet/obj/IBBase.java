package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.obj.scope.IBScopeContainer;

/**
 * Created by CJ Burkey on 2018/11/12
 */
public interface IBBase {
    
    void setNamespace(BNamespace namespace);
    BNamespace getNamespace();
    
    void setParent(IBScopeContainer parent);
    IBScopeContainer getParent();
    
}
