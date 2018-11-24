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
@SuppressWarnings("unused")
public interface IScopeContainer extends IABase {
    
    default Optional<Collection<ANamespace>> getNamespaces() { return Optional.empty(); }
    default Optional<Collection<AClassDec>> getClassDecs() { return Optional.empty(); }
    default Optional<Collection<AFunctionDec>> getFunctionDecs() { return Optional.empty(); }
    default Optional<Collection<AVariableDec>> getVariableDecs() { return Optional.empty(); }
    default Optional<Collection<AStatement>> getStatements() { return Optional.empty(); }
    
    default void remove(ANamespace namespace) {
        getNamespaces().ifPresent(namespaces -> namespaces.remove(namespace));
    }
    default void remove(AClassDec classDec) {
        getClassDecs().ifPresent(classDecs -> classDecs.remove(classDec));
    }
    default void remove(AFunctionDec functionDec) {
        getFunctionDecs().ifPresent(functionDecs -> functionDecs.remove(functionDec));
    }
    default void remove(AVariableDec variableDec) {
        getVariableDecs().ifPresent(variableDecs -> variableDecs.remove(variableDec));
    }
    default void remove(AStatement statement) {
        getStatements().ifPresent(statements -> statements.remove(statement));
    }
    
    static void makeChildren(IScopeContainer container, ABase parent, Collection<? extends ABase> elements) {
        elements.forEach(e -> e.setScopeParent(container, parent));
    }
    
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void makeChild(IScopeContainer container, ABase parent, Optional<? extends ABase> element) {
        element.ifPresent(e -> e.setScopeParent(container, parent));
    }
    
}
