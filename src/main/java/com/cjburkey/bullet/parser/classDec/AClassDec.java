package com.cjburkey.bullet.parser.classDec;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import com.cjburkey.bullet.parser.type.ATypeFrag;
import com.cjburkey.bullet.parser.type.ATypes;
import com.cjburkey.bullet.parser.variable.AVariableDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class AClassDec extends ABase implements IScopeContainer {
    
    public final String identifier;
    public final Optional<ATypes> types;
    public final AClassMembers classMembers;
    
    public AClassDec(String identifier, Optional<ATypes> types, AClassMembers classMembers, BulletParser.ClassDecContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
        this.types = types;
        this.classMembers = classMembers;
    }
    
    public boolean getMatchesTypeExactly(ATypeFrag type) {
        return type.identifier.equals(identifier);
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Class:\n");
        
        output.append(getIndent(indent + indent())).append("Name:\n").append(getIndent(indent + indent() * 2)).append(identifier);
        types.ifPresent(aTypes -> output.append(aTypes.debug(indent + indent())));
        output.append(classMembers.debug(indent + indent()));
        return output.toString();
    }
    
    public void settleChildren() {
        IScopeContainer.makeChild(getScope(), this, types);
        classMembers.setScopeParent(this, this);
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        types.ifPresent(aTypes -> output.addAll(aTypes.verify()));
        output.addAll(classMembers.verify());
        return output;
    }
    
    public Optional<Collection<AVariableDec>> getVariableDecs() {
        ObjectArrayList<AVariableDec> variables = new ObjectArrayList<>();
        variables.addAll(classMembers.variableDecs);
        for (AFunctionDec functionDec : classMembers.functionDecs) {
            if (functionDec.isConstructor) {
                functionDec.getVariableDecs().ifPresent(variables::addAll);
            }
        }
        return Optional.of(variables);
    }
    
    public Optional<Collection<AFunctionDec>> getFunctionDecs() {
        return Optional.ofNullable(classMembers.functionDecs);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        types.ifPresent(aTypes -> output.addAll(aTypes.searchAndMerge()));
        output.addAll(classMembers.searchAndMerge());
        
        if (getScope() != null && getScope().getClassDecs().isPresent()) {
            for (AClassDec classDec : getScope().getClassDecs().get()) {
                // Merge classes of the same superclasses and with the same name
                if (classDec != this && classDec.identifier.equals(identifier) && classDec.types.equals(types)) {
                    classDec.classMembers.functionDecs.addAll(classMembers.functionDecs);
                    classDec.classMembers.variableDecs.addAll(classMembers.variableDecs);
                    
                    // Destroy self and remove from parent
                    getScope().remove(this);
                    classMembers.functionDecs.clear();
                    classMembers.variableDecs.clear();
                    setScopeParent(null, null);
                    
                    // Propogate changes through children
                    classDec.settleChildren();
                    return classDec.searchAndMerge();
                } else if (classDec != this && classDec.identifier.equals(identifier)) {
                    output.add(onDuplicate(classDec));
                    break;
                }
            }
        }
        return output;
    }
    
    private BulletError onDuplicate(AClassDec other) {
        ObjectArrayList<ATypeFrag> argTypes1 = new ObjectArrayList<>();
        other.types.ifPresent(aTypes -> argTypes1.addAll(aTypes.types));
        ObjectArrayList<ATypeFrag> argTypes2 = new ObjectArrayList<>();
        types.ifPresent(aTypes -> argTypes2.addAll(aTypes.types));
        return BulletError.format(ctx, "Classes with name \"%s\" have differing super classes of types: %s and %s", identifier,
                Arrays.toString(argTypes1.toArray(new ATypeFrag[0])), Arrays.toString(argTypes2.toArray(new ATypeFrag[0])));
    }
    
}
