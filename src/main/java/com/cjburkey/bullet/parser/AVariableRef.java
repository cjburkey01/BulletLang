package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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