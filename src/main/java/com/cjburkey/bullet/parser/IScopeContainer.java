package com.cjburkey.bullet.parser;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/11/21
 */
public interface IScopeContainer {
    
    static void makeChildren(IScopeContainer container, ABase parent, Collection<? extends ABase> elements) {
        elements.forEach(e -> e.setScopeParent(container, parent));
    }
    
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void makeChild(IScopeContainer container, ABase parent, Optional<? extends ABase> element) {
        element.ifPresent(e -> e.setScopeParent(container, parent));
    }
    
}
