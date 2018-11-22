package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
    
    // TODO: CHECK VARIABLE TYPES
    public ObjectArrayList<BulletVerifyError> verify() {
        return exprList.verify();
    }
    
}
