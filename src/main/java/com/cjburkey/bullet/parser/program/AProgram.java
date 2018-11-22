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
import java.util.Collection;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public class AProgram extends ABase implements IScopeContainer {
    
    public final Optional<ARequirements> requirements;
    public final Optional<AProgramIn> programIn;
    
    public AProgram(Optional<ARequirements> requirements, Optional<AProgramIn> programIn, BulletParser.ProgramContext ctx) {
        super(ctx);
        
        this.requirements = requirements;
        this.programIn = programIn;
    }
    
    public String getFormattedDebug(int indent) {
        StringBuilder output = new StringBuilder();
        
        output.append(getIndent(indent));
        output.append("Program:\n");
        requirements.ifPresent(aRequirements -> output.append(aRequirements.debug(indent + indent())));
        programIn.ifPresent(aProgramIn -> output.append(aProgramIn.debug(indent + indent())));
        return output.toString();
    }
    
    public void settleChildren() {
        IScopeContainer.makeChild(this, this, requirements);
        IScopeContainer.makeChild(this, this, programIn);
    }
    
    public ObjectArrayList<BulletVerifyError> verify() {
        ObjectArrayList<BulletVerifyError> output = requirements.map(ARequirements::verify).orElseGet(ObjectArrayList::new);
        output.addAll(programIn.map(AProgramIn::verify).orElseGet(ObjectArrayList::new));
        return output;
    }
    
    public Optional<Collection<ANamespace>> getNamespaces() {
        return programIn.map(aProgramIn -> aProgramIn.namespaces);
    }
    
    public Optional<Collection<AFunctionDec>> getFunctionDecs() {
        return programIn.map(aProgramIn -> aProgramIn.functions);
    }
    
    public Optional<Collection<AClassDec>> getClassDecs() {
        return programIn.map(aProgramIn -> aProgramIn.classes);
    }
    
    public Optional<Collection<AStatement>> getStatements() {
        return programIn.map(aProgramIn -> aProgramIn.statements);
    }
    
    public ObjectArrayList<BulletVerifyError> searchAndMerge() {
        ObjectArrayList<BulletVerifyError> output = new ObjectArrayList<>();
        requirements.ifPresent(aRequirements -> output.addAll(aRequirements.searchAndMerge()));
        programIn.ifPresent(aProgramIn -> output.addAll(aProgramIn.searchAndMerge()));
        return output;
    }
    
}
