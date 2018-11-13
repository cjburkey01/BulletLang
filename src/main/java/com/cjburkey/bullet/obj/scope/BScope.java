package com.cjburkey.bullet.obj.scope;

import com.cjburkey.bullet.obj.BNamespace;
import com.cjburkey.bullet.obj.IBBase;
import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("unused")
public final class BScope implements IBBase {
    
    private BNamespace namespace;
    private IBScopeContainer parent;
    public final List<BStatement> statements = new ArrayList<>();
    
    public String toString() {
        return Arrays.toString(statements.toArray(new BStatement[0]));
    }
    
    public void setNamespace(BNamespace namespace) {
        this.namespace = namespace;
        for (BStatement statement : statements) {
            statement.setNamespace(namespace);
        }
    }
    
    public BNamespace getNamespace() {
        return namespace;
    }
    
    public void setParent(IBScopeContainer parent) {
        this.parent = parent;
        for (BStatement statement : statements) {
            statement.setParent(parent);
        }
    }
    
    public IBScopeContainer getParent() {
        return parent;
    }
    
}
