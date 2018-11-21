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
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("IfStatement:\n");
        output.append(getIndent(indent + indent()));
        output.append("IsElse:\n");
        output.append(getIndent(indent + indent() * 2));
        output.append(isElse);
        output.append('\n');
        
        expression.ifPresent(aExpression -> output.append(aExpression.debug(indent + indent())));
        output.append(statements.debug(indent + indent()));
        return output.toString();
    }
    
}
