package com.cjburkey.bullet.parser.namespace;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AVariableDec;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.classDec.AClassDec;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/19
 */
public class ANamespaceIn extends ABase {
    
    public final ObjectArrayList<AVariableDec> variableDecs = new ObjectArrayList<>();
    public final ObjectArrayList<AFunctionDec> functionDecs = new ObjectArrayList<>();
    public final ObjectArrayList<AClassDec> classDecs = new ObjectArrayList<>();
    
    public ANamespaceIn(BulletParser.NamespaceInContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        output.append(getIndent(indent));
        output.append("ProgramIn:\n");
        
        indent += indent();
        
        output.append(getIndent(indent));
        output.append("Functions:\n");
        for (AFunctionDec function : functionDecs) {
            output.append(function.debug(indent + indent()));
        }
        output.append(getIndent(indent));
        output.append("Classes:\n");
        for (AClassDec classDec : classDecs) {
            output.append(classDec.debug(indent + indent()));
        }
        output.append(getIndent(indent));
        output.append("VariableDecs:\n");
        for (AVariableDec variableDec : variableDecs) {
            output.append(variableDec.debug(indent + indent()));
        }
        return output.toString();
    }
    
    public void settleChildren() {
        IScopeContainer.makeChildren(getScope(), this, variableDecs);
        IScopeContainer.makeChildren(getScope(), this, functionDecs);
        IScopeContainer.makeChildren(getScope(), this, classDecs);
    }
    
    public ObjectArrayList<BulletVerifyError> searchAndMerge() {
        ObjectArrayList<BulletVerifyError> output = new ObjectArrayList<>();
        variableDecs.forEach(variableDec -> output.addAll(variableDec.searchAndMerge()));
        functionDecs.forEach(functionDec -> output.addAll(functionDec.searchAndMerge()));
        classDecs.forEach(classDec -> output.addAll(classDec.searchAndMerge()));
        return output;
    }
    
    @SuppressWarnings("unchecked")
    public ObjectArrayList<BulletVerifyError> verify() {
        return verifyLists(variableDecs, functionDecs, classDecs);
    }
    
}
