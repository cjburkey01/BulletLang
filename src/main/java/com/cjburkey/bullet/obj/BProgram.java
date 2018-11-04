package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BProgram extends BBase implements IBScope {
    
    public final String namespace;
    public final List<BFunction> functions = new ArrayList<>();
    public final List<BStatement> statements = new ArrayList<>();
    
    public BProgram(BulletParser.ProgramContext ctx) {
        super(ctx);
        
        namespace = (ctx.namespace() == null || ctx.namespace().IDENTIFIER() == null) ? "" : ctx.namespace().IDENTIFIER().getText();
    }
    
    public List<BStatement> getStatements() {
        return statements;
    }
    
}
