package com.cjburkey.bullet.parser.expression;

import com.cjburkey.bullet.BulletLang;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.type.ATypeFrag;
import com.cjburkey.bullet.parser.type.ATypeDec;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.visitor.ParserVisitor;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CJ Burkey on 2018/11/20
 */
@SuppressWarnings("WeakerAccess")
public class AString extends AExpression {
    
    private static final Pattern internalExpressionPattern = Pattern.compile("#\\{.*?}");
    
    public final boolean isSmart;
    public final String string;
    public final Int2ObjectLinkedOpenHashMap<AExpression> smartInsertionPoints = new Int2ObjectLinkedOpenHashMap<>();
    
    public AString(boolean isSmart, String string, BulletParser.StringContext ctx) {
        super(ctx);
        
        this.isSmart = isSmart;
        string = string.substring(1, string.length() - 1);
        this.string = isSmart ? loadSmartString(string.substring(1)) : string;
    }
    
    public AString(String string, BulletParser.LiteralStringContext ctx) {
        super(ctx);
        
        this.isSmart = false;
        this.string = string.substring(3, string.length() - 3);
    }
    
    private String loadSmartString(String input) {
        StringBuilder output = new StringBuilder(input);
        Matcher matcher = internalExpressionPattern.matcher(input);
        if (matcher.find()) {
            // Get the expression string
            String str = matcher.group(0);
            str = str.substring(2, str.length() - 1);
            
            // Delete it from the old string
            output.delete(matcher.start(), matcher.end());
            
            // Build a temporary parser and parse the expression
            BulletParser parser = BulletLang.buildQuickParser(str);
            BulletParser.PartialExpContext ctx = parser.partialExp();
            if (ctx == null) {
                onInvalidSmartString(str);
            }
            Optional<AExpression> expression = ParserVisitor._expressionVisitor.visit(ctx);
            if (!expression.isPresent()) {
                onInvalidSmartString(str);
            }
            
            // Mark the point as an expression insertion point
            smartInsertionPoints.put(matcher.start(), expression.get());
            
            // Continue searching this string for smart string values
            return loadSmartString(output.toString());
        }
        return output.toString();
    }
    
    public String getFormattedDebug(int indent) {
        // Show insertion points as '<>' character in the string for debugging
        StringBuilder str = new StringBuilder(string);
        if (isSmart) {
            int[] points = smartInsertionPoints.keySet().toIntArray();
            for (int i = 0; i < points.length; i ++) {
                str.insert((i * 2) + points[i], "<>");
            }
        }
        
        StringBuilder output = new StringBuilder();
        output.append(getIndent(indent));
        output.append("String:\n");
        output.append(getIndent(indent + indent()));
        output.append('"');
        output.append(str);
        output.append('"');
        output.append('\n');
        output.append(getIndent(indent));
        output.append("IsSmart:\n");
        output.append(getIndent(indent + indent()));
        output.append(isSmart);
        output.append('\n');
        if (isSmart) {
            output.append(getIndent(indent));
            output.append("InsertionExpressions:\n");
            for (AExpression expression : smartInsertionPoints.values()) {
                output.append(expression.debug(indent + indent()));
            }
        }
        
        return output.toString();
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        smartInsertionPoints.values().forEach(expression -> output.addAll(expression.searchAndMerge()));
        return output;
    }
    
    public void settleChildren() {
        IScopeContainer.makeChildren(getScope(), this, smartInsertionPoints.values());
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        smartInsertionPoints.values().forEach(expression -> output.addAll(expression.verify()));
        return output;
    }
    
    private static void onInvalidSmartString(String text) {
        throw new IllegalStateException(String.format("Invalid expression in smart string: \"%s\"", text));
    }
    
    public Optional<ATypeDec> resolveType() {
        return Optional.of(ATypeDec.getPlain("String", ctx));
    }
    
}
