package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/21
 */
@SuppressWarnings("unused")
public interface IABase {
    
    void setScopeParent(IScopeContainer scope, ABase parent);
    IScopeContainer getScope();
    ABase getParent();
    String debug(int indent);
    String getFormattedDebug(int indent);
    
    // Sets all children's parents to self and scope to current scope where applicable
    void settleChildren();
    
    // Merge and error scopes
    ObjectArrayList<BulletError> searchAndMerge();
    
    // Checks for errors before compilation
    ObjectArrayList<BulletError> verify();
    
}
