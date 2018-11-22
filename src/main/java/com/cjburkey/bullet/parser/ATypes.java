package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class ATypes extends ABase {
    
    public final ObjectArrayList<String> types = new ObjectArrayList<>();
    
    public ATypes(BulletParser.TypesContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Types:\n");
        
        for (String type : types) {
            output.append(getIndent(indent + indent()));
            output.append("Type:\n");
            output.append(getIndent(indent + indent() * 2));
            output.append(type);
            output.append('\n');
        }
        return output.toString();
    }
    
    public void settleChildren() {
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        return new ObjectArrayList<>();
    }
    
}
