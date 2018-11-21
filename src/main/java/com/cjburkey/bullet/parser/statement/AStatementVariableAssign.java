package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AVariableAssign;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AStatementVariableAssign extends AStatement {
    
    public final AVariableAssign variableAssign;
    
    public AStatementVariableAssign(AVariableAssign variableAssign, BulletParser.StatementVariableAssignContext ctx) {
        super(ctx);
        
        this.variableAssign = variableAssign;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "StatementVariableAssign:\n" + variableAssign.debug(indent + indent());
    }
    
}
