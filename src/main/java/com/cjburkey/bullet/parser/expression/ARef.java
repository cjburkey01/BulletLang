package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AReference;
import com.cjburkey.bullet.parser.ATypeDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/23
 */
@SuppressWarnings("WeakerAccess")
public class ARef extends AExpression {
    
    public final AReference reference;
    
    public ARef(AReference reference, BulletParser.RefContext ctx) {
        super(ctx);
        
        this.reference = reference;
    }
    
    public String getFormattedDebug(int indent) {
        return reference.debug(indent);
    }
    
    public void settleChildren() {
        reference.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return reference.searchAndMerge();
    }
    
    public ObjectArrayList<BulletError> verify() {
        return reference.verify();
    }
    
    public ATypeDec resolveType() {
        return reference.resolveType();
    }
    
}
