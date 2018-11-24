package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class ATypeDec extends ABase {
    
    // TODO: UNION TYPES
    public final AType type;
    public final Optional<AArrayType> arrayType;
    
    public ATypeDec(AType type, Optional<AArrayType> arrayType, ParserRuleContext ctx) {
        super(ctx);
        
        this.type = type;
        this.arrayType = arrayType;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "TypeDec:\n" + type.debug(indent + indent()) + '\n' + 
                arrayType.map(aArrayType -> aArrayType.debug(indent + indent())).orElse("");
    }
    
    public void settleChildren() {
        type.setScopeParent(getScope(), this);
        IScopeContainer.makeChild(getScope(), this, arrayType);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(type.searchAndMerge());
        arrayType.ifPresent(aArrayType -> output.addAll(aArrayType.searchAndMerge()));
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(type.verify());
        arrayType.ifPresent(aArrayType -> output.addAll(aArrayType.verify()));
        return output;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATypeDec aTypeDec = (ATypeDec) o;
        return type.equals(aTypeDec.type) &&
                arrayType.equals(aTypeDec.arrayType);
    }
    
    public int hashCode() {
        return Objects.hash(type, arrayType);
    }
    
    public String toString() {
        return type + (arrayType.isPresent() ? "[]" : "");
    }
    
    public static ATypeDec getVoid(ParserRuleContext ctx) {
        return new ATypeDec(new AType("Void", ctx), Optional.empty(), ctx);
    }
    
}
