package com.cjburkey.bullet.parser.type;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.ABase;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ATypeDec extends ABase {
    
    // TODO: UNION TYPES
    public final ATypeWhole typeWhole;
    
    public ATypeDec(ATypeWhole typeWhole, ParserRuleContext ctx) {
        super(ctx);
        
        this.typeWhole = typeWhole;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "TypeDec:\n" + typeWhole.debug(indent + indent());
    }
    
    public void settleChildren() {
        typeWhole.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return typeWhole.searchAndMerge();
    }
    
    public ObjectArrayList<BulletError> verify() {
        return typeWhole.verify();
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATypeDec aTypeDec = (ATypeDec) o;
        return typeWhole.equals(aTypeDec.typeWhole);
    }
    
    public int hashCode() {
        return Objects.hash(typeWhole);
    }
    
    public static ATypeDec getPlain(String identifier, ParserRuleContext ctx) {
        return new ATypeDec(new ATypeWhole(Optional.of(new ATypeHalf(new ATypeFrag(identifier, ctx), Optional.empty(), ctx)),
                Optional.empty(), ctx), ctx);
    }
    
    public static ATypeDec getVoid(ParserRuleContext ctx) {
        return getPlain("Void", ctx);
    }
    
}
