package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.scope.BScope;
import com.cjburkey.bullet.obj.scope.IBScopeContainer;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BIfStatement extends BStatement implements IBScopeContainer {
    
    public boolean isElse;
    public final BExpression condition;
    public final BScope scope = new BScope();
    
    public BIfStatement(boolean isElse, BExpression condition, List<BStatement> statements, BulletParser.IfStatementContext ctx) {
        super(ctx);
        
        this.isElse = isElse;
        this.condition = condition;
        scope.statements.addAll(statements);
    }
    
    public BScope getScope() {
        return scope;
    }
    
    public String toString() {
        if (isElse && condition != null) {
            return String.format("Else if [%s] then execute %s", condition, scope);
        }
        if (isElse) {
            return String.format("Else execute %s", scope);
        }
        return String.format("If [%s] then execute %s", condition, scope);
    }
    
}
