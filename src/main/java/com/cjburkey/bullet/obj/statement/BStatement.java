package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.obj.BBase;
import com.cjburkey.bullet.obj.IBScope;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BStatement extends BBase {
    
    public final IBScope parentScope;
    
    public BStatement(IBScope parentScope, ParserRuleContext ctx) {
        super(ctx);
        
        this.parentScope = parentScope;
    }
    
}
