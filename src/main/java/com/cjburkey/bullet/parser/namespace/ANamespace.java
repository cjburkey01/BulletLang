package com.cjburkey.bullet.parser.namespace;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings("WeakerAccess")
public class ANamespace extends ABase {
    
    public final String identifier;
    public final ANamespaceIn namespaceIn;
    
    public ANamespace(String identifier, ANamespaceIn namespaceIn, BulletParser.NamespaceContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
        this.namespaceIn = namespaceIn;
    }
    
}
