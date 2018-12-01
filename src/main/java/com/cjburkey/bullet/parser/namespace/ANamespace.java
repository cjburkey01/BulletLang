package com.cjburkey.bullet.parser.namespace;

import com.cjburkey.bullet.BulletError;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.IScopeContainer;
import com.cjburkey.bullet.parser.classDec.AClassDec;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import com.cjburkey.bullet.parser.variable.AVariableDec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/19
 */
@SuppressWarnings("WeakerAccess")
public class ANamespace extends ABase implements IScopeContainer {
    
    public final String identifier;
    public final ANamespaceIn namespaceIn;
    
    public ANamespace(String identifier, ANamespaceIn namespaceIn, BulletParser.NamespaceContext ctx) {
        super(ctx);
        
        this.identifier = identifier;
        this.namespaceIn = namespaceIn;
    }
    
    public String getFormattedDebug(int indent) {
        return getIndent(indent) + "Namespace \"" + identifier + "\":\n" + namespaceIn.debug(indent + indent());
    }
    
    public void settleChildren() {
        namespaceIn.setScopeParent(this, this);
    }
    
    public ObjectArrayList<BulletError> searchAndMerge() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(namespaceIn.searchAndMerge());
        return output;
    }
    
    public ObjectArrayList<BulletError> verify() {
        ObjectArrayList<BulletError> output = new ObjectArrayList<>();
        output.addAll(namespaceIn.verify());
        return output;
    }
    
    public Optional<Collection<AVariableDec>> getVariableDecs() {
        return Optional.of(namespaceIn.variableDecs);
    }
    
    public Optional<Collection<AFunctionDec>> getFunctionDecs() {
        return Optional.of(namespaceIn.functionDecs);
    }
    
    public Optional<Collection<AClassDec>> getClassDecs() {
        return Optional.of(namespaceIn.classDecs);
    }
    
}
