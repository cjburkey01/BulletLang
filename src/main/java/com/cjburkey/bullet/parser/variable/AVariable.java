package com.cjburkey.bullet.parser.variable;

import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.type.ATypeDec;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/23
 */
public abstract class AVariable extends ABase {
    
    public final String identifier;
    
    public AVariable(String identifier, ParserRuleContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
    }
    
    public abstract Optional<ATypeDec> resolveType();
    
}
