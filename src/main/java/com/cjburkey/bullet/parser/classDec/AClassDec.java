package com.cjburkey.bullet.parser.classDec;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AName;
import com.cjburkey.bullet.parser.ATypes;
import com.cjburkey.bullet.parser.AVariableDec;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
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
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = name.verify();
        types.ifPresent(aTypes -> output.addAll(aTypes.verify()));
        classMembers.ifPresent(aClassMembers -> output.addAll(aClassMembers.verify()));
        return output;
    }
    
    public Optional<Collection<AVariableDec>> getVariableDecs() {
        ObjectArrayList<AVariableDec> variables = new ObjectArrayList<>();
        if (classMembers.isPresent()) {
            variables.addAll(classMembers.get().variableDecs);
            for (AFunctionDec functionDec : classMembers.get().functionDecs) {
                if (functionDec.isConstructor) {
                    functionDec.getVariableDecs().ifPresent(variables::addAll);
                }
            }
        }
        return Optional.of(variables);
    }
    
    public Optional<Collection<AFunctionDec>> getFunctionDecs() {
        return classMembers.map(aClassMembers -> aClassMembers.functionDecs);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(name.searchAndMerge());
        types.ifPresent(aTypes -> output.addAll(aTypes.searchAndMerge()));
        classMembers.ifPresent(aClassMembers -> output.addAll(aClassMembers.searchAndMerge()));
        
        if (getScope() != null && getScope().getClassDecs().isPresent()) {
            for (AClassDec classDec : getScope().getClassDecs().get()) {
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
                    return classDec.searchAndMerge();
                } else if (classDec != this && classDec.name.equals(name)) {
                    output.add(onDuplicate(classDec));
                    break;
                }
            }
        }
        return output;
    }
    
    private BulletError onDuplicate(AClassDec other) {
        ObjectArrayList<String> argTypes1 = new ObjectArrayList<>();
        other.types.ifPresent(aTypes -> argTypes1.addAll(aTypes.types));
        ObjectArrayList<String> argTypes2 = new ObjectArrayList<>();
        types.ifPresent(aTypes -> argTypes2.addAll(aTypes.types));
        return BulletError.format(ctx, "Classes with name \"%s\" have differing super classes of types: %s and %s", name,
                Arrays.toString(argTypes1.toArray(new String[0])), Arrays.toString(argTypes2.toArray(new String[0])));
    }
    
}
