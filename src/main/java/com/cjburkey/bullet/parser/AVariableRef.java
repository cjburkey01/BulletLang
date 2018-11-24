package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AVariableRef extends ABase {
    
    public final AVariableType variableType;
    public final AName name;
    
    public AVariableRef(int variableType, AName name, ParserRuleContext ctx) {
        super(ctx);
        
        this.variableType = AVariableType.fromId(variableType).orElse(null);
        this.name = name;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "VariableRef:\n" + getIndent(indent + indent()) + "VariableType:\n" +
                getIndent(indent + indent() * 2) +  variableType + '\n' + name.debug(indent + indent());
    }
    
    public void settleChildren() {
        name.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return name.searchAndMerge();
    }
    
    public ObjectArrayList<BulletError> verify() {
        return new ObjectArrayList<>();
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AVariableRef that = (AVariableRef) o;
        return variableType == that.variableType &&
                name.equals(that.name);
    }
    
    public int hashCode() {
        return Objects.hash(variableType, name);
    }
    
    public String toString() {
        String type = "";
        if (variableType.equals(AVariableType.LOCAL)) {
            type = "@";
        } else if (variableType.equals(AVariableType.STATIC)) {
            type = "@@";
        }
        return type + name;
    }
    
    @SuppressWarnings("unused")
    public enum AVariableType {
        GLOBAL,
        LOCAL,
        STATIC,
        
        ;
        
        public static Optional<AVariableType> fromId(int id) {
            if (id < 0 || id >= values().length) {
                return Optional.empty();
            }
            return Optional.of(values()[id]);
        }
    }
    
}
