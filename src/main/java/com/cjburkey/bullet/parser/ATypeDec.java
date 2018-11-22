package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class ATypeDec extends ABase {
    
    public final String identifier;
    public final Optional<AArrayType> arrayType;
    
    public ATypeDec(String identifier, Optional<AArrayType> arrayType, BulletParser.TypeDecContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
        this.arrayType = arrayType;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "TypeDec:\n" + getIndent(indent + indent()) + identifier + '\n' + 
                arrayType.map(aArrayType -> aArrayType.debug(indent + indent())).orElse("");
    }
    
    public void settleChildren() {
        IScopeContainer.makeChild(getScope(), this, arrayType);
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        return new ObjectArrayList<>();
    }
    
}
