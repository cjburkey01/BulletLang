package com.cjburkey.bullet.parser.variable;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.ABase;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AVariableRef extends ABase {
    
    public final AVariableType variableType;
    public final String identifier;
    
    public AVariableRef(int variableType, String identifier, ParserRuleContext ctx) {
        super(ctx);
        
        this.variableType = AVariableType.fromId(variableType).orElse(null);
        this.identifier = identifier;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "VariableRef \"" + identifier+ "\":\n" + getIndent(indent + indent()) + "VariableType:\n" +
                getIndent(indent + indent() * 2) +  variableType + '\n';
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
        AVariableRef that = (AVariableRef) o;
        return variableType == that.variableType &&
                identifier.equals(that.identifier);
    }
    
    public int hashCode() {
        return Objects.hash(variableType, identifier);
    }
    
    public String toString() {
        String type = "";
        if (variableType.equals(AVariableType.LOCAL)) {
            type = "@";
        } else if (variableType.equals(AVariableType.STATIC)) {
            type = "@@";
        }
        return type + identifier;
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
