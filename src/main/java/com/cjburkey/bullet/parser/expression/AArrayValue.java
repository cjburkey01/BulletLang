package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.AExprList;
import com.cjburkey.bullet.parser.type.ATypeDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/21
 */
@SuppressWarnings("WeakerAccess")
public class AArrayValue extends AExpression {
    
    public final AExprList exprList;
    
    public AArrayValue(AExprList exprList, BulletParser.ArrayValueContext ctx) {
        super(ctx);
        
        this.exprList = exprList;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "ArrayValue:\n" + exprList.debug(indent + indent());
    }
    
    public void settleChildren() {
        exprList.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return exprList.searchAndMerge();
    }
    
    // TODO: CHECK VARIABLE TYPES
    public ObjectArrayList<BulletError> verify() {
        return exprList.verify();
    }
    
    // TODO
    public Optional<ATypeDec> resolveType() {
        return Optional.empty();
    }
    
}
