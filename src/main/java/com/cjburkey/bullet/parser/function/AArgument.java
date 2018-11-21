package com.cjburkey.bullet.parser.function;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.ATypeDec;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class AArgument extends ABase {
    
    public final AName name;
    public final Optional<ATypeDec> typeDec;
    
    public AArgument(AName name, Optional<ATypeDec> typeDec, BulletParser.ArgumentContext ctx) {
        super(ctx);
        
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
    
}
