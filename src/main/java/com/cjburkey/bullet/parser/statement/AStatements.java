package com.cjburkey.bullet.parser.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AStatements extends ABase {
    
    public final List<AStatement> statements = new ArrayList<>();
    
    public AStatements(BulletParser.StatementsContext ctx) {
        super(ctx);
    }
    
}
