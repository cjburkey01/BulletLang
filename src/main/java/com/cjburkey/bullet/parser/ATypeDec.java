package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ATypeDec extends ABase {
    
    public final String identifier;
    
    public ATypeDec(String identifier, BulletParser.TypeDecContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "TypeDec:\n" + getIndent(indent + indent()) + identifier + '\n';
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        return new ObjectArrayList<>();
    }
    
}
