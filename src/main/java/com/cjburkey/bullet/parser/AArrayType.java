package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/21
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class AArrayType extends ABase {
    
    public final Optional<AExpression> expression;
    
    public AArrayType(Optional<AExpression> expression, BulletParser.ArrayTypeContext ctx) {
        super(ctx);
        
        this.expression = expression;
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        output.append(getIndent(indent));
        output.append("Array");
        if (expression.isPresent()) {
            output.append("Type:\n");
            output.append(expression.get().debug(indent + indent()));
        } else {
            output.append('\n');
        }
        return output.toString();
    }
    
    // TODO: ENSURE EXPRESSION RESOLVES TO INTEGER
    public ObjectArrayList<BulletVerifyError> verify() {
        return expression.map(AExpression::verify).orElseGet(ObjectArrayList::new);
    }
    
}
