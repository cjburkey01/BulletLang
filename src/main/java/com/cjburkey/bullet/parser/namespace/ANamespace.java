package com.cjburkey.bullet.parser.namespace;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "Namespace:\n" + name.debug(indent + indent()) + namespaceIn.debug(indent + indent());
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        ObjectArrayList<BulletVerifyError> output = name.verify();
        output.addAll(namespaceIn.verify());
        return output;
    }
    
}
