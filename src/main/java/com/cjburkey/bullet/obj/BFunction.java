package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.statement.BArgument;
import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class BFunction implements IBScope {
    
    public final String name;
    public final String type;
    public final List<BArgument> arguments = new ArrayList<>();
    public final List<BStatement> statements = new ArrayList<>();
    
    public BFunction(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    public BFunction(BulletParser.FunctionContext ctx) {
        this(ctx.IDENTIFIER().getText(), getType(ctx));
    }
    
    private static String getType(BulletParser.FunctionContext ctx) {
        if (ctx.functionType() != null && ctx.functionType().IDENTIFIER() != null){
            return ctx.functionType().IDENTIFIER().getText();
        }
        return "Void";
    }
    
    public List<BStatement> getStatements() {
        return statements;
    }
    
}
