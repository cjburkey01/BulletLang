package com.cjburkey.bullet.parser.requirement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ARequirements extends ABase {
    
    public final List<ARequirement> requirements = new ArrayList<>();
    
    public ARequirements(BulletParser.RequirementsContext ctx) {
        super(ctx);
    }
    
}
