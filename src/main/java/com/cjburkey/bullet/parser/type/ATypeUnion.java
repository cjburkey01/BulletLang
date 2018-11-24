package com.cjburkey.bullet.parser.type;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.ABase;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Arrays;
import java.util.Objects;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/24
 */
public class ATypeUnion extends ABase {
    
    public final ObjectOpenHashSet<ATypeHalf> typeHalfs = new ObjectOpenHashSet<>();
    
    public ATypeUnion(ParserRuleContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "UnionType:\n" + getIndent(indent + indent())
                + Arrays.toString(typeHalfs.toArray(new ATypeHalf[0]));
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
        ATypeUnion that = (ATypeUnion) o;
        return typeHalfs.equals(that.typeHalfs);
    }
    
    public int hashCode() {
        return Objects.hash(typeHalfs);
    }
    
}
