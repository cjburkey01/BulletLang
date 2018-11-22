package com.cjburkey.bullet.parser.function;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AArguments extends ABase {
    
    public final ObjectArrayList<AArgument> arguments = new ObjectArrayList<>();
    
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
    
    public void settleChildren() {
        IScopeContainer.makeChildren(getScope(), this, arguments);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        arguments.forEach(argument -> output.addAll(argument.searchAndMerge()));
        return output;
    }
    
    @SuppressWarnings("unchecked")
    public ObjectArrayList<BulletError> verify() {
        return verifyLists(arguments);
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AArguments that = (AArguments) o;
        return arguments.equals(that.arguments);
    }
    
    public int hashCode() {
        return Objects.hash(arguments);
    }
    
}
