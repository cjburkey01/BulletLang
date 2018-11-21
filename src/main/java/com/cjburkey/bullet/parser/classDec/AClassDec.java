package com.cjburkey.bullet.parser.classDec;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.ATypes;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public class AClassDec extends ABase {
    
    public final AName name;
    public final Optional<ATypes> types;
    public final Optional<AClassMembers> classMembers;
    
    public AClassDec(AName name, Optional<ATypes> types, Optional<AClassMembers> classMembers, BulletParser.ClassDecContext ctx) {
        super(ctx);
        
        this.name = name;
        this.types = types;
        this.classMembers = classMembers;
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Class:\n");
        
        output.append(name.getFormattedDebug(indent + indent()));
        types.ifPresent(aTypes -> output.append(aTypes.debug(indent + indent())));
        classMembers.ifPresent(aClassMembers -> output.append(aClassMembers.debug(indent + indent())));
        return output.toString();
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        ObjectArrayList<BulletVerifyError> output = name.verify();
        types.ifPresent(aTypes -> output.addAll(aTypes.verify()));
        classMembers.ifPresent(aClassMembers -> output.addAll(aClassMembers.verify()));
        return output;
    }
    
}
