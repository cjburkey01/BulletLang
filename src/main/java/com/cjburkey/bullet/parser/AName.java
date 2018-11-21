package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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
    
    public ObjectArrayList<BulletVerifyError> verify() {
        return new ObjectArrayList<>();
    }
    
}
