package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.statement.FunctionDec;
import com.cjburkey.bullet.parser.component.statement.Statement;
import com.cjburkey.bullet.parser.component.statement.VariableDec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Scope extends Base {

    public final ObjectArrayList<Statement> statements = new ObjectArrayList<>();
    public final Object2ObjectOpenHashMap<String, ObjectArrayList<FunctionDec>> functions = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<String, ObjectArrayList<VariableDec>> variables = new Object2ObjectOpenHashMap<>();

    public void addFunction(FunctionDec functionDec) {
        add(functions, functionDec.name, functionDec);
    }

    public void addVariable(VariableDec variableDec) {
        add(variables, variableDec.name, variableDec);
    }

    private <T> void add(Object2ObjectOpenHashMap<String, ObjectArrayList<T>> values, String name, T newValue) {
        ObjectArrayList<T> at = values.get(name);
        if (at == null) {
            at = new ObjectArrayList<>();
            values.put(name, at);
        }
        if (!at.contains(newValue)) {
            at.add(newValue);
        }
    }

    public static final class Visitor extends BaseV<Scope> {

        public Visitor(Scope scope) {
            super(scope);
        }

        public Optional<Scope> visitScope(BulletLangParser.ScopeContext ctx) {
            Scope scope = new Visitor(this.scope)
                    .visit(ctx.scope())
                    .orElseGet(Scope::new);
            Optional<Statement> statement = new Statement.Visitor(scope)
                    .visit(ctx.statement());
            statement.ifPresent(scope.statements::add);

            scope.parentScope = this.scope;
            return Optional.of(scope);
        }

    }

}
