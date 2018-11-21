package com.cjburkey.bullet.parser.function;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AArguments extends ABase {
    
    public final List<AArgument> arguments = new ArrayList<>();
    
    public AArguments(BulletParser.ArgumentsContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Arguments:\n");
        
        for (AArgument argument : arguments) {
            output.append(argument.debug(indent + indent()));
        }
        return output.toString();
    }
    
}
