package com.cjburkey.bullet.parser.program;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.classDec.AClassDec;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import com.cjburkey.bullet.parser.namespace.ANamespace;
import com.cjburkey.bullet.parser.statement.AStatement;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class AProgramIn extends ABase {
    
    public final ObjectArrayList<ANamespace> namespaces = new ObjectArrayList<>();
    public final ObjectArrayList<AFunctionDec> functions = new ObjectArrayList<>();
    public final ObjectArrayList<AClassDec> classes = new ObjectArrayList<>();
    public final ObjectArrayList<AStatement> statements = new ObjectArrayList<>();
    
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
        output.append("Functions:\n");
        for (AFunctionDec function : functions) {
            output.append(function.debug(indent + indent()));
        }
        output.append(getIndent(indent));
        output.append("Classes:\n");
        for (AClassDec classDec : classes) {
            output.append(classDec.debug(indent + indent()));
        }
        output.append(getIndent(indent));
        output.append("Statements:\n");
        for (AStatement statement : statements) {
            output.append(statement.debug(indent + indent()));
        }
        return output.toString();
    }
    
    public void settleChildren() {
        IScopeContainer.makeChildren(getScope(), this, namespaces);
        IScopeContainer.makeChildren(getScope(), this, functions);
        IScopeContainer.makeChildren(getScope(), this, classes);
        IScopeContainer.makeChildren(getScope(), this, statements);
    }
    
    @SuppressWarnings("unchecked")
    public ObjectArrayList<BulletVerifyError> verify() {
        return verifyLists(namespaces, functions, classes, statements);
    }
    
}
