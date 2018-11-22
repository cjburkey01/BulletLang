package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AIfStatement;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
        return ifStatement.debug(indent);
    }
    
    public void settleChildren() {
        ifStatement.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletVerifyError> searchAndMerge() {
        return ifStatement.searchAndMerge();
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        return ifStatement.verify();
    }
    
}
