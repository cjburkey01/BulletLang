package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.variable.AVariableAssign;
import com.cjburkey.bullet.BulletError;
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
    
    public void settleChildren() {
        variableAssign.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return variableAssign.searchAndMerge();
    }
    
    public ObjectArrayList<BulletError> verify() {
        return variableAssign.verify();
    }
    
}
