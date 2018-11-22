package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public abstract class AStatement extends ABase implements IScopeContainer {
    
    public AStatement(BulletParser.StatementContext ctx) {
        super(ctx);
    }
    
}
