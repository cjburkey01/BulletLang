package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AVariableDec;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AStatementVariableDec extends AStatement {
    
    public final AVariableDec variableDec;
    
    public AStatementVariableDec(AVariableDec variableDec, BulletParser.StatementVariableDecContext ctx) {
        super(ctx);
        
        this.variableDec = variableDec;
    }
    
    public String getFormattedDebug(int indent) {
        return variableDec.debug(indent);
    }
    
    public void settleChildren() {
        variableDec.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletVerifyError> searchAndMerge() {
        return variableDec.searchAndMerge();
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        return variableDec.verify();
    }
    
}
