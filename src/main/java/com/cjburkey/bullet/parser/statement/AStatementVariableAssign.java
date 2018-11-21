package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AVariableAssign;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
        return variableAssign.debug(indent);
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        return variableAssign.verify();
    }
    
}
