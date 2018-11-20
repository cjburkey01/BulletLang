package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.AVariableRef;
import com.cjburkey.bullet.parser.function.AFuncParams;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class AReference extends AExpression {
    
    public boolean isFunctionRef;
    public final Optional<AExpression> expression;
    public final Optional<AName> name;
    public final Optional<AVariableRef> variableRef;
    public final Optional<AFuncParams> funcParams;
    
    public AReference(Optional<AExpression> expression, AName name, Optional<AFuncParams> funcParams,
                         BulletParser.FunctionReferenceContext ctx) {
        super(ctx);
        
        this.isFunctionRef = true;
        this.expression = expression;
        this.name = Optional.of(name);
        this.variableRef = Optional.empty();
        this.funcParams = funcParams;
    }
    
    public AReference(Optional<AExpression> expression, AVariableRef variableRef, BulletParser.ReferenceContext ctx) {
        super(ctx);
        
        this.isFunctionRef = false;
        this.expression = expression;
        this.name = Optional.empty();
        this.variableRef = Optional.of(variableRef);
        this.funcParams = Optional.empty();
    }
    
}
