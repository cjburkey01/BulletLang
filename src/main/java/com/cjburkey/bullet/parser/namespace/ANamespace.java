package com.cjburkey.bullet.parser.namespace;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AName;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings("WeakerAccess")
public class ANamespace extends ABase {
    
    public final AName name;
    public final ANamespaceIn namespaceIn;
    
    public ANamespace(AName name, ANamespaceIn namespaceIn, BulletParser.NamespaceContext ctx) {
        super(ctx);
        
        this.name = name;
        this.namespaceIn = namespaceIn;
    }
    
}
