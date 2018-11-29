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
@SuppressWarnings("WeakerAccess")
public class ATypeFrag extends ABase {
    
    public final String identifier;
    
    public ATypeFrag(String identifier, ParserRuleContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
    }
    
    public Optional<AClassDec> resolveTypeDeclaration(IScopeContainer startingPoint) {
        while (startingPoint != null) {
            if (startingPoint.getClassDecs().isPresent()) {
                for (AClassDec classDec : startingPoint.getClassDecs().get()) {
                    if (classDec.getMatchesTypeExactly(this)) return Optional.of(classDec);
                }
            }
            startingPoint = startingPoint.getScope();
        }
        return Optional.empty();
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "Type:\n" + getIndent(indent + indent()) + identifier + '\n';
    }
    
    public void settleChildren() {
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return new ObjectArrayList<>();
    }
    
    public ObjectArrayList<BulletError> verify() {
        return new ObjectArrayList<>();
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATypeFrag aTypeFrag = (ATypeFrag) o;
        return identifier.equals(aTypeFrag.identifier);
    }
    
    public int hashCode() {
        return Objects.hash(identifier);
    }
    
    public String toString() {
        return identifier;
    }
    
}