package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AScope extends ABase {
    
    public final ObjectArrayList<AStatement> statements = new ObjectArrayList<>();
    
    public AScope(ParserRuleContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Statements:\n");
        
        for (AStatement statement : statements) {
            output.append(statement.debug(indent + indent()));
        }
        return output.toString();
    }
    
    public void settleChildren() {
        IScopeContainer.makeChildren(getScope(), this, statements);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        statements.forEach(statement -> output.addAll(statement.searchAndMerge()));
        return output;
    }
    
    @SuppressWarnings("unchecked")
    public ObjectArrayList<BulletError> verify() {
        return verifyLists(statements);
    }
    
}
