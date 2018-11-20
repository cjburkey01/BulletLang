package com.cjburkey.bullet.parser.namespace;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AContent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings("WeakerAccess")
public class ANamespaceIn extends ABase {
    
    public final List<AContent> contents = new ArrayList<>();
    
    public ANamespaceIn(Collection<AContent> contents, BulletParser.NamespaceContext ctx) {
        super(ctx);
        
        this.contents.addAll(contents);
    }
    
}
