package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AIfStatement;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AStatementIf extends AStatement {
    
    public final AIfStatement ifStatement;
    
    public AStatementIf(AIfStatement ifStatement, BulletParser.StatementIfContext ctx) {
        super(ctx);
        
        this.ifStatement = ifStatement;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "StatementIf:\n" + ifStatement.debug(indent + indent());
    }
    
}
