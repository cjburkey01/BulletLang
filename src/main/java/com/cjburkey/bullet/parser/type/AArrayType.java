package com.cjburkey.bullet.parser.type;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.expression.AExpression;
import com.cjburkey.bullet.BulletError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Objects;
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
    
    public void settleChildren() {
        IScopeContainer.makeChild(getScope(), this, expression);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return expression.map(AExpression::searchAndMerge).orElseGet(ObjectArrayList::new);
    }
    
    // TODO: ENSURE EXPRESSION RESOLVES TO INTEGER
    public ObjectArrayList<BulletError> verify() {
        return expression.map(AExpression::verify).orElseGet(ObjectArrayList::new);
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AArrayType that = (AArrayType) o;
        return expression.equals(that.expression);
    }
    
    public int hashCode() {
        return Objects.hash(expression);
    }
    
}
