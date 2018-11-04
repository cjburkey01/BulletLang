package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.obj.BBase;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public abstract class BStatement extends BBase {
    
    public BStatement(ParserRuleContext ctx) {
        super(ctx);
    }
    
}
