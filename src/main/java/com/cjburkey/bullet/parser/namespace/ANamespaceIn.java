package com.cjburkey.bullet.parser.namespace;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AContent;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ANamespaceIn extends ABase {
    
    public final ObjectArrayList<AContent> contents = new ObjectArrayList<>();
    
    public ANamespaceIn(BulletParser.NamespaceInContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("NamespaceIn:\n");
        
        indent += indent();
        
        output.append(getIndent(indent));
        output.append("Contents:\n");
        for (AContent content : contents) {
            output.append(content.debug(indent + indent()));
        }
        return output.toString();
    }
    
    @SuppressWarnings("unchecked")
    public ObjectArrayList<BulletVerifyError> verify() {
        return verifyLists(contents);
    }
    
}
