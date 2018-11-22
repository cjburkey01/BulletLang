package com.cjburkey.bullet.parser.program;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ARequirement extends ABase {
    
    public final String string;
    
    public ARequirement(String string, BulletParser.RequirementContext ctx) {
        super(ctx);
        
        this.string = string;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + string + '\n';
    }
    
    public void settleChildren() {
    }
    
    public ObjectArrayList<BulletVerifyError> searchAndMerge() {
        return new ObjectArrayList<>();
    }
    
    // TODO: CHECK IF FILE EXISTS IN SOURCE OR AS ABSOLUTE
    public ObjectArrayList<BulletVerifyError> verify() {
        return new ObjectArrayList<>();
    }
    
}
