package com.cjburkey.bullet.parser.program;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.program.requirement.ARequirements;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public class AProgram extends ABase {
    
    public final Optional<ARequirements> requirements;
    public final Optional<AProgramIn> programIn;
    
    public AProgram(Optional<ARequirements> requirements, Optional<AProgramIn> programIn, BulletParser.ProgramContext ctx) {
        super(ctx);
        
        this.requirements = requirements;
        this.programIn = programIn;
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Program:\n");
        requirements.ifPresent(aRequirements -> output.append(aRequirements.debug(indent + indent())));
        programIn.ifPresent(aProgramIn -> output.append(aProgramIn.debug(indent + indent())));
        return output.toString();
    }
    
}
