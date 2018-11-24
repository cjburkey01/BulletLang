package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.ATypeDec;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public abstract class AExpression extends ABase {
    
    public AExpression(BulletParser.ExpressionContext ctx) {
        super(ctx);
    }
    
    public abstract Optional<ATypeDec> resolveType();
    
}
