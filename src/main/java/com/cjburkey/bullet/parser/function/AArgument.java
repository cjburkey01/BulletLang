package com.cjburkey.bullet.parser.function;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.type.ATypeDec;
import com.cjburkey.bullet.parser.AVariable;
import com.cjburkey.bullet.parser.IScopeContainer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
public class AArgument extends AVariable {
    
    public final AName name;
    public final Optional<ATypeDec> typeDec;
    
    public AArgument(AName name, Optional<ATypeDec> typeDec, BulletParser.ArgumentContext ctx) {
        super(name, ctx);
        
        this.name = name;
        this.typeDec = typeDec;
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Argument:\n");
        
        output.append(name.getFormattedDebug(indent + indent()));
        typeDec.ifPresent(aTypeDec -> output.append(aTypeDec.debug(indent + indent())));
        return output.toString();
    }
    
    public void settleChildren() {
        name.setScopeParent(getScope(), this);
        IScopeContainer.makeChild(getScope(), this, typeDec);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(name.searchAndMerge());
        typeDec.ifPresent(aTypeDec -> output.addAll(aTypeDec.searchAndMerge()));
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = name.verify();
        typeDec.ifPresent(aTypeDec -> output.addAll(aTypeDec.verify()));
        return output;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AArgument argument = (AArgument) o;
        return name.equals(argument.name) &&
                typeDec.equals(argument.typeDec);
    }
    
    public int hashCode() {
        return Objects.hash(name, typeDec);
    }
    
    public Optional<ATypeDec> resolveType() {
        if (typeDec.isPresent()) return typeDec;
        // TODO
        return Optional.empty();
    }
    
}
