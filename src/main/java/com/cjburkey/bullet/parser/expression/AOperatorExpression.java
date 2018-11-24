package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AExprList;
import com.cjburkey.bullet.parser.AOperator;
import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.parser.AReference;
import com.cjburkey.bullet.parser.ATypeDec;
import com.cjburkey.bullet.parser.classDec.AClassDec;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/21
 */
@SuppressWarnings("WeakerAccess")
public abstract class AOperatorExpression extends AExpression {
    
    public final AExpression expressionA;
    public final AOperator operator;
    
    protected AReference functionReference;
    
    public AOperatorExpression(AExpression expressionA, AOperator operator, BulletParser.UnaryOpContext ctx) {
        super(ctx);
        
        this.expressionA = expressionA;
        this.operator = operator;
    }
    
    public AOperatorExpression(AExpression expressionA, AOperator operator, BulletParser.BinaryOpContext ctx) {
        super(ctx);
        
        this.expressionA = expressionA;
        this.operator = operator;
    }
    
    public AReference getFunctionReference() {
        if (functionReference == null) {
            functionReference = new AReference(operator, (BulletParser.UnaryOpContext) ctx);
        }
        return functionReference;
    }
    
    public void settleChildren() {
        expressionA.setScopeParent(getScope(), this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        return expressionA.searchAndMerge();
    }
    
    public ObjectArrayList<BulletError> verify() {
        return expressionA.verify();
    }
    
    public Optional<ATypeDec> resolveType() {
        // Get the type of the first term
        Optional<ATypeDec> typeDec = expressionA.resolveType();
        if (!typeDec.isPresent()) return Optional.empty();
        
        // Find the function for the first term
        AClassDec classDec = typeDec.get().type.resolveTypeDeclaration(getScope()).orElse(null);
        if (classDec == null || !classDec.getFunctionDecs().isPresent()) return Optional.empty();
        for (AFunctionDec functionDec : classDec.getFunctionDecs().get()) {
            if (functionDec.getFullMatches(operator.token, getParameters())) {
                return Optional.of(functionDec.typeDec);
            }
        }
        return Optional.empty();
    }
    
    protected abstract Optional<AExprList> getParameters();
    
}
