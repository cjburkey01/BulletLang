package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AName extends ABase {
    
    public final String identifier;
    
    public AName(AOperator operator, ParserRuleContext ctx) {
        super(ctx);
        
        this.identifier = operator.token;
    }
    
    public AName(String identifier, BulletParser.NameContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "Name:\n" + getIndent(indent + indent()) + identifier + '\n';
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
        AName aName = (AName) o;
        return identifier.equals(aName.identifier);
    }
    
    public int hashCode() {
        return Objects.hash(identifier);
    }
    
}
