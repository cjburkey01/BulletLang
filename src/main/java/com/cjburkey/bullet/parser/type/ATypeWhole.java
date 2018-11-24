package com.cjburkey.bullet.parser.type;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.classDec.AClassDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/24
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class ATypeWhole extends ABase {
    
    public final Optional<ATypeHalf> typeHalf;
    public final Optional<ATypeUnion> typeUnion;
    
    public ATypeWhole(Optional<ATypeHalf> typeHalf, Optional<ATypeUnion> typeUnion, ParserRuleContext ctx) {
        super(ctx);
        
        this.typeHalf = typeHalf;
        this.typeUnion = typeUnion;
    }
    
    public Optional<ObjectArrayList<AClassDec>> resolveTypeDeclaration(IScopeContainer startingPoint) {
        ObjectArrayList<AClassDec> output = new ObjectArrayList<>();
        if (typeHalf.isPresent()) {
            typeHalf.get().typeFrag.resolveTypeDeclaration(startingPoint).ifPresent(output::add);
            return output.size() == 0 ? Optional.empty() : Optional.of(output);
        }
        if (!typeUnion.isPresent()) return Optional.empty();
        for (ATypeHalf typeHalf : typeUnion.get().typeHalfs) {
            AClassDec classDec = typeHalf.typeFrag.resolveTypeDeclaration(startingPoint).orElse(null);
            if (classDec == null) return Optional.empty();
            output.add(classDec);
        }
        return output.size() == 0 ? Optional.empty() : Optional.of(output);
    }
    
    @SuppressWarnings("unused")
    public boolean getIsUnion() {
        return typeUnion.isPresent();
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "TypeWhole:\n" + typeHalf.map(aTypeHalf -> aTypeHalf.debug(indent + indent())).orElse("")
                + typeUnion.map(aTypeUnion -> aTypeUnion.debug(indent + indent())).orElse("");
    }
    
    public void settleChildren() {
        IScopeContainer.makeChild(getScope(), this, typeHalf);
        IScopeContainer.makeChild(getScope(), this, typeUnion);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        typeHalf.ifPresent(aTypeHalf -> output.addAll(aTypeHalf.searchAndMerge()));
        typeUnion.ifPresent(aTypeUnion -> output.addAll(aTypeUnion.searchAndMerge()));
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        typeHalf.ifPresent(aTypeHalf -> output.addAll(aTypeHalf.verify()));
        typeUnion.ifPresent(aTypeUnion -> output.addAll(aTypeUnion.verify()));
        return output;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATypeWhole that = (ATypeWhole) o;
        return typeHalf.equals(that.typeHalf) &&
                typeUnion.equals(that.typeUnion);
    }
    
    public int hashCode() {
        return Objects.hash(typeHalf, typeUnion);
    }
    
}
