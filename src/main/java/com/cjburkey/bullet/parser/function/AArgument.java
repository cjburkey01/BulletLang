package com.cjburkey.bullet.parser.function;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.type.ATypeDec;
import com.cjburkey.bullet.parser.variable.AVariable;
import com.cjburkey.bullet.parser.IScopeContainer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
public class AArgument extends AVariable {
    
    public final Optional<ATypeDec> typeDec;
    
    public AArgument(String identifier, Optional<ATypeDec> typeDec, BulletParser.ArgumentContext ctx) {
        super(identifier, ctx);
        
        this.typeDec = typeDec;
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Argument:\n");
        
        output.append(getIndent(indent + indent())).append("Name:\n").append(getIndent(indent + indent() * 2)).append(identifier);
        typeDec.ifPresent(aTypeDec -> output.append(aTypeDec.debug(indent + indent())));
        return output.toString();
    }
    
    public void settleChildren() {
        IScopeContainer.makeChild(getScope(), this, typeDec);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        typeDec.ifPresent(aTypeDec -> output.addAll(aTypeDec.searchAndMerge()));
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        typeDec.ifPresent(aTypeDec -> output.addAll(aTypeDec.verify()));
        return output;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AArgument argument = (AArgument) o;
        return identifier.equals(argument.identifier) &&
                typeDec.equals(argument.typeDec);
    }
    
    public int hashCode() {
        return Objects.hash(identifier, typeDec);
    }
    
    public Optional<ATypeDec> resolveType() {
        if (typeDec.isPresent()) return typeDec;
        // TODO
        return Optional.empty();
    }
    
}
