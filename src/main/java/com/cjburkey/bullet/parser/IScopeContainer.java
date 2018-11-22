package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.parser.classDec.AClassDec;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import com.cjburkey.bullet.parser.namespace.ANamespace;
import com.cjburkey.bullet.parser.statement.AStatement;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/21
 */
public interface IScopeContainer {
    
    default Optional<Collection<ANamespace>> getNamespaces() { return Optional.empty(); }
    default Optional<Collection<AClassDec>> getClassDecs() { return Optional.empty(); }
    default Optional<Collection<AFunctionDec>> getFunctionDecs() { return Optional.empty(); }
    default Optional<Collection<AVariableDec>> getVariableDecs() { return Optional.empty(); }
    default Optional<Collection<AStatement>> getStatements() { return Optional.empty(); }
    
    static void makeChildren(IScopeContainer container, ABase parent, Collection<? extends ABase> elements) {
        elements.forEach(e -> e.setScopeParent(container, parent));
    }
    
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void makeChild(IScopeContainer container, ABase parent, Optional<? extends ABase> element) {
        element.ifPresent(e -> e.setScopeParent(container, parent));
    }
    
}
