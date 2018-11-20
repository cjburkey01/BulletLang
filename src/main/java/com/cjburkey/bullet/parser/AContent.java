package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public class AContent extends ABase {
    
    public final Optional<AVariableDec> variableDec;
    public final Optional<AFunctionDec> functionDec;
    public final Optional<AClassDec> classDec;
    
    private AContent(AVariableDec variableDec, AFunctionDec functionDec, AClassDec classDec, BulletParser.ContentContext ctx) {
        super(ctx);
        
        this.variableDec = variableDec == null ? Optional.empty() : Optional.of(variableDec);
        this.functionDec = functionDec == null ? Optional.empty() : Optional.of(functionDec);
        this.classDec = classDec == null ? Optional.empty() : Optional.of(classDec);
    }
    
    public AContent(AVariableDec variableDec, BulletParser.ContentContext ctx) {
        this(variableDec, null, null, ctx);
    }
    
    public AContent(AFunctionDec functionDec, BulletParser.ContentContext ctx) {
        this(null, functionDec, null, ctx);
    }
    
    public AContent(AClassDec classDec, BulletParser.ContentContext ctx) {
        this(null, null, classDec, ctx);
    }
    
}
