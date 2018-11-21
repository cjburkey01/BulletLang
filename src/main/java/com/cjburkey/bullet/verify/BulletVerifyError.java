package com.cjburkey.bullet.verify;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/21
 */
public class BulletVerifyError {
    
    public final String printText;
    public final ParserRuleContext ctx;
    
    public BulletVerifyError(String printText, ParserRuleContext ctx) {
        this.printText = printText;
        this.ctx = ctx;
    }
    
}
