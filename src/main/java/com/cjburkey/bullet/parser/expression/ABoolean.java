package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class ABoolean extends AExpression {
    
    public final boolean bool;
    
    public ABoolean(boolean bool, BulletParser.BooleanContext ctx) {
        super(ctx);
        
        this.bool = bool;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "Boolean:\n" + getIndent(indent + indent()) + bool + '\n';
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