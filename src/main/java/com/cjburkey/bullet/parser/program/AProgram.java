package com.cjburkey.bullet.parser.program;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings("WeakerAccess")
public class AProgram extends ABase {
    
    public final AProgramIn programIn;
    
    public AProgram(AProgramIn programIn, BulletParser.ProgramContext ctx) {
        super(ctx);
        
        this.programIn = programIn;
    }
    
}
