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
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("ProgramIn:\n");
        
        indent += indent();
        
        output.append(getIndent(indent));
        output.append("Namespaces:\n");
        for (ANamespace namespace : namespaces) {
            output.append(namespace.debug(indent + indent()));
        }
        output.append(getIndent(indent));
        output.append("Contents:\n");
        for (AContent content : contents) {
            output.append(content.debug(indent + indent()));
        }
        output.append(getIndent(indent));
        output.append("Statements:\n");
        for (AStatement statement : statements) {
            output.append(statement.debug(indent + indent()));
        }
        return output.toString();
    }
    
}
