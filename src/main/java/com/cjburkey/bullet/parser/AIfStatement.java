package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.parser.statement.AStatements;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class AIfStatement extends ABase {
    
    public final boolean isElse;
    public final Optional<AExpression> expression;
    public final AStatements statements;
    
    public AIfStatement(boolean isElse, Optional<AExpression> expression, AStatements statements, BulletParser.IfStatementContext ctx) {
        super(ctx);
        
        this.isElse = isElse;
        this.expression = expression;
        this.statements = statements;
    }
    
}
