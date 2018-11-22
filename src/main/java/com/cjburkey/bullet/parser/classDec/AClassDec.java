package com.cjburkey.bullet.parser.classDec;

import com.cjburkey.bullet.Log;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.ATypes;
import com.cjburkey.bullet.parser.AVariableDec;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import com.cjburkey.bullet.verify.BulletVerifyError;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public class AClassDec extends ABase implements IScopeContainer {
    
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
    
    public void settleChildren() {
        name.setScopeParent(getScope(), this);
        types.ifPresent(aTypes -> aTypes.setScopeParent(getScope(), this));
        classMembers.ifPresent(aClassMembers -> aClassMembers.setScopeParent(this, this));
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        ObjectArrayList<BulletVerifyError> output = name.verify();
        types.ifPresent(aTypes -> output.addAll(aTypes.verify()));
        classMembers.ifPresent(aClassMembers -> output.addAll(aClassMembers.verify()));
        return output;
    }
    
    public Optional<Collection<AVariableDec>> getVariableDecs() {
        return classMembers.map(aClassMembers -> aClassMembers.variableDecs);
    }
    
    public Optional<Collection<AFunctionDec>> getFunctionDecs() {
        return classMembers.map(aClassMembers -> aClassMembers.functionDecs);
    }
    
    public ObjectArrayList<BulletVerifyError> searchAndMerge() {
        final IScopeContainer parentScope = getScope();
        if (parentScope != null && parentScope.getClassDecs().isPresent()) {
            for (AClassDec classDec : parentScope.getClassDecs().get()) {
                // Merge classes of the same superclasses and with the same name
                if (classDec != this && classDec.name.equals(name) && classDec.types.equals(types)) {
                    classDec.classMembers.ifPresent(aClassMembers -> {
                        classMembers.ifPresent(aClassMembers1 -> aClassMembers.functionDecs.addAll(aClassMembers1.functionDecs));
                        classMembers.ifPresent(aClassMembers1 -> aClassMembers.variableDecs.addAll(aClassMembers1.variableDecs));
                    });
                    
                    // Destroy self and remove from parent
                    getScope().remove(this);
                    classMembers.ifPresent(aClassMembers -> {
                        aClassMembers.functionDecs.clear();
                        aClassMembers.variableDecs.clear();
                    });
                    setScopeParent(null, null);
                    
                    // Propogate changes through children
                    classDec.settleChildren();
                    return classDec.searchAndMerge();   // TODO: TRY TO PREVENT EXTRA INVOCATIONS OF searchAndMerge?
                }
            }
        }
        
        ObjectArrayList<BulletVerifyError> output = name.searchAndMerge();
        types.ifPresent(aTypes -> output.addAll(aTypes.searchAndMerge()));
        classMembers.ifPresent(aClassMembers -> output.addAll(aClassMembers.searchAndMerge()));
        return output;
    }
    
}
