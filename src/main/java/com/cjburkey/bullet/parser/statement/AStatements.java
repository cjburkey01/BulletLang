package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AStatements extends ABase {
    
    public final ObjectArrayList<AStatement> statements = new ObjectArrayList<>();
    
    public AStatements(BulletParser.StatementsContext ctx) {
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
    
    @SuppressWarnings("unchecked")
    public ObjectArrayList<BulletVerifyError> verify() {
        return verifyLists(statements);
    }
    
}
