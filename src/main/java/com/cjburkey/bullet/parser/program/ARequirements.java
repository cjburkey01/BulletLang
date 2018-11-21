package com.cjburkey.bullet.parser.program;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ARequirements extends ABase {
    
    public final ObjectArrayList<ARequirement> requirements = new ObjectArrayList<>();
    
    public ARequirements(BulletParser.RequirementsContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Requirements:\n");
        for (ARequirement requirement : requirements) {
            output.append(requirement.debug(indent + indent()));
        }
        return output.toString();
    }
    
}
