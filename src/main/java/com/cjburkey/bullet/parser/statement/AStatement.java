package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings("WeakerAccess")
public abstract class AStatement extends ABase {
    
    public AStatement(BulletParser.StatementContext ctx) {
        super(ctx);
    }
    
}
