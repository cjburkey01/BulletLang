package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AExprList extends ABase {
    
    public final ObjectArrayList<AExpression> expressions = new ObjectArrayList<>();
    
    public AExprList(ParserRuleContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("ExprList:\n");
        for (AExpression expression : expressions) {
            output.append(expression.debug(indent + indent()));
        }
        return output.toString();
    }
    
    public void settleChildren() {
        IScopeContainer.makeChildren(getScope(), this, expressions);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        expressions.forEach(expression -> output.addAll(expression.searchAndMerge()));
        return output;
    }
    
    @SuppressWarnings("unchecked")
    public ObjectArrayList<BulletError> verify() {
        return verifyLists(expressions);
    }
    
}
