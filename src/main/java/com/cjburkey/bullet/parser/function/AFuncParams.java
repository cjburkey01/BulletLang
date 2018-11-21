package com.cjburkey.bullet.parser.function;

import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.expression.AExpression;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AFuncParams extends ABase {
    
    public final List<AExpression> expressions = new ArrayList<>();
    
    public AFuncParams(ParserRuleContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("FuncParams:\n");
        for (AExpression expression : expressions) {
            output.append(expression.debug(indent + indent()));
        }
        return output.toString();
    }
    
}
