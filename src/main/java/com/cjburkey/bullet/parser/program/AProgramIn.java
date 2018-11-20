package com.cjburkey.bullet.parser.program;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AContent;
import com.cjburkey.bullet.parser.namespace.ANamespace;
import com.cjburkey.bullet.parser.statement.AStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class AProgramIn extends ABase {
    
    public final List<ANamespace> namespaces = new ArrayList<>();
    public final List<AContent> contents = new ArrayList<>();
    public final List<AStatement> statements = new ArrayList<>();
    
    public AProgramIn(BulletParser.ProgramInContext ctx) {
        super(ctx);
    }
    
}
