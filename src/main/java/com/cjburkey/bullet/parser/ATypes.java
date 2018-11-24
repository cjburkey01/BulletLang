package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class ATypes extends ABase {
    
    public final ObjectArrayList<AType> types = new ObjectArrayList<>();
    
    public ATypes(BulletParser.TypesContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Types:\n");
        
        for (AType type : types) {
            output.append(getIndent(indent + indent()));
            output.append("Type:\n");
            output.append(getIndent(indent + indent() * 2));
            output.append(type);
            output.append('\n');
        }
        return output.toString();
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
        ATypes aTypes = (ATypes) o;
        return types.equals(aTypes.types);
    }
    
    public int hashCode() {
        return Objects.hash(types);
    }
    
}
