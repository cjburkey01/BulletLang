package com.cjburkey.bullet.parser.requirement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings("WeakerAccess")
public class BRequirements extends ABase {
    
    public final List<BRequirement> requirements = new ArrayList<>();
    
    public BRequirements(Collection<BRequirement> requirements, BulletParser.RequirementsContext ctx) {
        super(ctx);
        
        this.requirements.addAll(requirements);
    }
    
}
