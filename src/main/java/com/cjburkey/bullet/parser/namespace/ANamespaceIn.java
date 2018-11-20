package com.cjburkey.bullet.parser.namespace;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AContent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ANamespaceIn extends ABase {
    
    public final List<AContent> contents = new ArrayList<>();
    
    public ANamespaceIn(BulletParser.NamespaceInContext ctx) {
        super(ctx);
    }
    
}
