package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AStatementReturn extends AStatement {
    
    public final AExpression expression;
    
    public AStatementReturn(AExpression expression, BulletParser.StatementReturnContext ctx) {
        super(ctx);
        
        this.expression = expression;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "StatementReturn:\n" + expression.debug(indent + indent());
    }
    
    public void settleChildren() {
        expression.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletVerifyError> searchAndMerge() {
        return expression.searchAndMerge();
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        return expression.verify();
    }
    
}
