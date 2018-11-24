package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/24
 */
public class AType extends ABase {
    
    public final String identifier;
    
    public AType(String identifier, ParserRuleContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
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
        AType aType = (AType) o;
        return identifier.equals(aType.identifier);
    }
    
    public int hashCode() {
        return Objects.hash(identifier);
    }
    
    public String toString() {
        return identifier;
    }
    
}
