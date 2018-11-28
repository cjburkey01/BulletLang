package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.BulletLang;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AExprList;
import com.cjburkey.bullet.parser.AOperator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AUnaryOperator extends AOperatorExpression {
    
    public AUnaryOperator(AExpression expression, AOperator operator, BulletParser.UnaryOpContext ctx) {
        super(expression, operator, ctx);
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "UnaryOperator:\n" + expressionA.debug(indent + indent()) + operator.debug(indent + indent());
    }
    
    public ObjectArrayList<BulletError> verify() {
        return super.verify();
    }
    
    protected Optional<AExprList> getParameters() {
        AExprList out = new AExprList(ctx);
        if (BulletLang.process(out)) return Optional.of(out);
        return Optional.empty();
    }
    
}
