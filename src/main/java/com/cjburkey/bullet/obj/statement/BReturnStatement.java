package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;

/**
 * Created by CJ Burkey on 2018/11/09
 */
@SuppressWarnings("WeakerAccess")
public class BReturnStatement extends BStatement {
    
    public final BExpression value;
    
    public BReturnStatement(BExpression value, BulletParser.StatementReturnContext ctx) {
        super(ctx);
        
        this.value = value;
    }
    
    public String toString() {
        return String.format("Return: [%s]", value);
    }
    
}
