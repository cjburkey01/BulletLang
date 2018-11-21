package com.cjburkey.bullet.parser.classDec;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AVariableDec;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AClassMembers extends ABase {
    
    public final ObjectArrayList<AVariableDec> variableDecs = new ObjectArrayList<>();
    public final ObjectArrayList<AFunctionDec> functionDecs = new ObjectArrayList<>();
    
    public AClassMembers(BulletParser.ClassMembersContext ctx) {
        super(ctx);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("ClassMembers:\n");
        
        for (AVariableDec variableDec : variableDecs) {
            output.append(variableDec.debug(indent + indent()));
        }
        for (AFunctionDec functionDec : functionDecs) {
            output.append(functionDec.debug(indent + indent()));
        }
        return output.toString();
    }
    
    @SuppressWarnings("unchecked")
    public ObjectArrayList<BulletVerifyError> verify() {
        return verifyLists(variableDecs, functionDecs);
    }
    
}
