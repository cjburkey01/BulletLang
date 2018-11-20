package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.expression.AExpression;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public class AVariableDec extends ABase {
    
    public final AVariableType variableType;
    public final String identifier;
    public final Optional<ATypeDec> typeDec;
    public final Optional<AExpression> expression;
    
    public AVariableDec(AVariableType variableType, String identifier, ATypeDec typeDec, AExpression expression, BulletParser.VariableDecContext ctx) {
        super(ctx);
        
        this.variableType = variableType;
        this.identifier = identifier;
        this.typeDec = typeDec == null ? Optional.empty() : Optional.of(typeDec);
        this.expression = expression == null ? Optional.empty() : Optional.of(expression);
    }
    
    public enum AVariableType {
        GLOBAL,
        LOCAL,
        STATIC,
        
        ;
        
        public static Optional<AVariableType> fromId(int id) {
            if (id < 0 || id >= AVariableType.values().length) {
                return Optional.empty();
            }
            return Optional.of(AVariableType.values()[id]);
        }
    }
    
}
