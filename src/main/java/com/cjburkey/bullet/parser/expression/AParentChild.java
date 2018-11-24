package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AReference;
import com.cjburkey.bullet.parser.ATypeDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/23
 */
@SuppressWarnings("WeakerAccess")
public class AParentChild extends AExpression {
    
    public final AExpression expression;
    public final AReference reference;
    
    public AParentChild(AExpression expression, AReference reference, BulletParser.ParentChildContext ctx) {
        super(ctx);
        
        this.expression = expression;
        this.reference = reference;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "ParentChild:\n" + getIndent(indent + indent()) + "Expression:\n" +
                expression.debug(indent + indent() * 2) + getIndent(indent + indent()) + "Reference:\n" +
                reference.debug(indent + indent() * 2);
    }
    
    public void settleChildren() {
        expression.setScopeParent(getScope(), this);
        reference.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = expression.searchAndMerge();
        output.addAll(reference.searchAndMerge());
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = expression.verify();
        output.addAll(reference.verify());
        return output;
    }
    
    public ATypeDec resolveType() {
        return reference.resolveType();
    }
    
}
