package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.classDec.AClassDec;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class AContent extends ABase {
    
    public final Optional<AVariableDec> variableDec;
    public final Optional<AFunctionDec> functionDec;
    public final Optional<AClassDec> classDec;
    
    public AContent(Optional<AVariableDec> variableDec, Optional<AFunctionDec> functionDec, Optional<AClassDec> classDec,
                    BulletParser.ContentContext ctx) {
        super(ctx);
        
        this.variableDec = variableDec;
        this.functionDec = functionDec;
        this.classDec = classDec;
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Content:\n");
        
        variableDec.ifPresent(aVariableDec -> output.append(aVariableDec.debug(indent + indent())));
        functionDec.ifPresent(aFunctionDec -> output.append(aFunctionDec.debug(indent + indent())));
        classDec.ifPresent(aClassDec -> output.append(aClassDec.debug(indent + indent())));
        return output.toString();
    }
    
}
