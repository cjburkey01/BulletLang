package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.obj.scope.IBScopeContainer;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BBase implements IBBase {
    
    public final int startLine;
    public final int endLine;
    public final int startCharPos;
    public final int endCharPos;
    
    private IBScopeContainer parent = null;
    private boolean isValid = true;
    private BNamespace namespace = null;
    
    public BBase(ParserRuleContext ctx) {
        startLine = ctx.start.getLine();
        startCharPos = ctx.start.getCharPositionInLine();
        endLine = ctx.stop.getLine();
        endCharPos = ctx.stop.getCharPositionInLine();
    }
    
    public BNamespace getNamespace() {
        return namespace;
    }
    
    public void setNamespace(BNamespace namespace) {
        this.namespace = namespace;
    }
    
    public boolean getIsValid() {
        return isValid;
    }
    
    public void makeInvalid() {
        isValid = false;
    }
    
    public IBScopeContainer getParent() {
        return parent;
    }
    
    public void setParent(IBScopeContainer parent) {
        this.parent = parent;
    }
    
}
