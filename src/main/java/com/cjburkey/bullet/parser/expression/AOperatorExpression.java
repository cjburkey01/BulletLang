package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.BulletLang;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.AExprList;
import com.cjburkey.bullet.parser.AOperator;
import com.cjburkey.bullet.parser.classDec.AClassDec;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import com.cjburkey.bullet.parser.type.ATypeDec;
import com.cjburkey.bullet.parser.type.ATypeHalf;
import com.cjburkey.bullet.parser.type.ATypeUnion;
import com.cjburkey.bullet.parser.type.ATypeWhole;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/21
 */
@SuppressWarnings("WeakerAccess")
public abstract class AOperatorExpression extends AExpression {
    
    public final AExpression expressionA;
    public final AOperator operator;
    
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
        
        // Get classes for the type (possibly union types)
        Optional<ObjectArrayList<AClassDec>> classesDec = typeDec.get().typeWhole.resolveTypeDeclaration(getScope());
        if (!classesDec.isPresent()) return Optional.empty();
        
        ObjectArrayList<ATypeHalf> typeHalfs = new ObjectArrayList<>();
        for (AClassDec classDec : classesDec.get()) {
            if (!classDec.getFunctionDecs().isPresent()) continue;
            for (AFunctionDec functionDec : classDec.getFunctionDecs().get()) {
                if (functionDec.getFullMatches(operator.token, getParameters())) {
                    ATypeWhole typeWhole = functionDec.typeDec.typeWhole;
                    typeWhole.typeUnion.ifPresent(aTypeUnion -> typeHalfs.addAll(aTypeUnion.typeHalfs));
                    typeWhole.typeHalf.ifPresent(typeHalfs::add);
                }
            }
        }
        if (typeHalfs.size() == 0) return Optional.empty();
        if (typeHalfs.size() == 1) {
            ATypeDec out = new ATypeDec(new ATypeWhole(Optional.of(typeHalfs.get(0)), Optional.empty(), ctx), ctx);
            if (BulletLang.process(out)) return Optional.of(out);
            return Optional.empty();
        }
        
        ATypeUnion typeUnion = new ATypeUnion(ctx);
        typeUnion.typeHalfs.addAll(typeHalfs);
        ATypeDec out = new ATypeDec(new ATypeWhole(Optional.empty(), Optional.of(typeUnion), ctx), ctx);
        if (BulletLang.process(out)) return Optional.of(out);
        return Optional.empty();
    }
    
    protected abstract Optional<AExprList> getParameters();
    
}
