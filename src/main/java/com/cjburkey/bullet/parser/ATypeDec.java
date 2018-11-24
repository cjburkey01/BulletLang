package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.classDec.AClassDec;
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
    public final String identifier;
    public final Optional<AArrayType> arrayType;
    
    public ATypeDec(String identifier, Optional<AArrayType> arrayType, ParserRuleContext ctx) {
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
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return arrayType.map(AArrayType::searchAndMerge).orElseGet(ObjectArrayList::new);
    }
    
    public ObjectArrayList<BulletError> verify() {
        return new ObjectArrayList<>();
    }
    
    public Optional<AClassDec> resolveTypeDeclaration(IScopeContainer startingPoint) {
        while (startingPoint != null) {
            if (startingPoint.getClassDecs().isPresent()) {
                for (AClassDec classDec : startingPoint.getClassDecs().get()) {
                    if (classDec.name.identifier.equals(identifier)) return Optional.of(classDec);
                }
            }
            startingPoint = startingPoint.getScope();
        }
        return Optional.empty();
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATypeDec aTypeDec = (ATypeDec) o;
        return identifier.equals(aTypeDec.identifier) &&
                arrayType.equals(aTypeDec.arrayType);
    }
    
    public int hashCode() {
        return Objects.hash(identifier, arrayType);
    }
    
    public String toString() {
        return identifier + (arrayType.isPresent() ? "[]" : "");
    }
    
    public static ATypeDec getVoid(ParserRuleContext ctx) {
        return new ATypeDec("Void", Optional.empty(), ctx);
    }
    
}
