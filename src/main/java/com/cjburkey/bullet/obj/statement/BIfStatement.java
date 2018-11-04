package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.IBScope;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BIfStatement extends BStatement implements IBScope {
    
    public boolean isElse;
    public final BExpression condition;
    public final List<BStatement> statements = new ArrayList<>();
    
    public BIfStatement(IBScope parentScope, BulletParser.StatementIfContext ctx) {
        super(parentScope, ctx);
        
        isElse = ctx.ifStatement().ELSE() != null;
        condition = ctx.ifStatement().expression() != null ? new BExpression(ctx.ifStatement().expression()) : null;
    }
    
    public List<BStatement> getStatements() {
        return statements;
    }
    
}
