package com.cjburkey.bullet.parser.type;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/24
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class ATypeHalf extends ABase {
    
    public final ATypeFrag typeFrag;
    public final Optional<AArrayType> arrayType;
    
    public ATypeHalf(ATypeFrag typeFrag, Optional<AArrayType> arrayType, ParserRuleContext ctx) {
        super(ctx);
        
        this.typeFrag = typeFrag;
        this.arrayType = arrayType;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "TypeHalf:\n" + typeFrag.debug(indent + indent())
                + arrayType.map((aArrayType -> aArrayType.debug(indent + indent()))).orElse("");
    }
    
    public void settleChildren() {
        typeFrag.setScopeParent(getScope(), this);
        IScopeContainer.makeChild(getScope(), this, arrayType);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(typeFrag.searchAndMerge());
        arrayType.ifPresent(aArrayType -> output.addAll(aArrayType.searchAndMerge()));
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(typeFrag.verify());
        arrayType.ifPresent(aArrayType -> output.addAll(aArrayType.verify()));
        return output;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATypeHalf aTypeHalf = (ATypeHalf) o;
        return typeFrag.equals(aTypeHalf.typeFrag) &&
                arrayType.equals(aTypeHalf.arrayType);
    }
    
    public int hashCode() {
        return Objects.hash(typeFrag, arrayType);
    }
    
    public String toString() {
        return typeFrag + arrayType.map(ignored -> "[]").orElse("");
    }
    
}
