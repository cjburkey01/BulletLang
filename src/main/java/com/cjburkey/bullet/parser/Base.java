package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.parser.component.Scope;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public abstract class Base implements IReferenceResolver, ITypeResolver {

    public Scope parentScope;
    public ParserRuleContext ctx;

    public Base(ParserRuleContext ctx) {
        this.ctx = ctx;
    }

}
