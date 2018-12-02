package com.cjburkey.bullet.parser.classDec;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/12/02
 */
public class ANativeType extends ABase {
    
    public final String identifier;
    
    public ANativeType(String identifier, BulletParser.NativeTypeContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "NativeType: \"" + identifier + "\"";
    }
    
    public void settleChildren() {
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return new ObjectArrayList<>();
    }
    
    public ObjectArrayList<BulletError> verify() {
        return new ObjectArrayList<>();
    }
    
}
