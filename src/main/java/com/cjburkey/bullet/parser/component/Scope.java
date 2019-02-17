package com.cjburkey.bullet.parser.component;

import com.cjburkey.bullet.antlr.BulletLangParser;
import com.cjburkey.bullet.parser.Base;
import com.cjburkey.bullet.parser.BaseV;
import com.cjburkey.bullet.parser.component.classes.ClassDec;
import com.cjburkey.bullet.parser.component.statement.FunctionDec;
import com.cjburkey.bullet.parser.component.statement.Statement;
import com.cjburkey.bullet.parser.component.statement.VariableDec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class Scope extends Base {

    public final ObjectArrayList<Statement> statements = new ObjectArrayList<>();
    private final Object2ObjectOpenHashMap<String, ObjectArrayList<FunctionDec>> functions = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<String, ObjectArrayList<VariableDec>> variables = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<String, ObjectArrayList<ClassDec>> classes = new Object2ObjectOpenHashMap<>();

    public Scope(ParserRuleContext ctx) {
        super(ctx);
    }

    public void addFunction(FunctionDec functionDec) {
        add(functions, functionDec.name, functionDec);
    }

    public void addVariable(VariableDec variableDec) {
        add(variables, variableDec.name, variableDec);
    }

    public void addClass(ClassDec classDec) {
        add(classes, classDec.name, classDec);
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

    private List<FunctionDec> getFunctions(String name, boolean scanParents) {
        if (!functions.containsKey(name)) return Collections.emptyList();
        ObjectArrayList<FunctionDec> output = new ObjectArrayList<>(functions.get(name));
        if (scanParents && parentScope != null) {
            output.addAll(parentScope.getFunctions(name, true));
        }
        return output;
    }

    public Optional<FunctionDec> getFunction(String name, Parameters parameters, boolean scanParents) {
        List<FunctionDec> functions = getFunctions(name, scanParents);
        for (FunctionDec functionDec : functions) {
            if (functionDec != null && functionDec.parameters != null && functionDec.parameters.equals(parameters)) {
                return Optional.of(functionDec);
            }
        }
        return Optional.empty();
    }

    private List<VariableDec> getVariables(String name, boolean scanParents) {
        if (!variables.containsKey(name)) return Collections.emptyList();
        ObjectArrayList<VariableDec> output = new ObjectArrayList<>(variables.get(name));
        if (scanParents && parentScope != null) {
            output.addAll(parentScope.getVariables(name, true));
        }
        return output;
    }

    public Optional<VariableDec> getVariable(String name, boolean scanParents) {
        List<VariableDec> variables = getVariables(name, scanParents);
        for (VariableDec variableDec : variables) {
            if (variableDec != null) {
                return Optional.of(variableDec);
            }
        }
        return Optional.empty();
    }

    private List<ClassDec> getClasses(String name, boolean scanParents) {
        if (!classes.containsKey(name)) return Collections.emptyList();
        ObjectArrayList<ClassDec> output = new ObjectArrayList<>(classes.get(name));
        if (scanParents && parentScope != null) {
            output.addAll(parentScope.getClasses(name, true));
        }
        return output;
    }

    public Optional<ClassDec> getClass(String name, boolean scanParents) {
        List<ClassDec> classes = getClasses(name, scanParents);
        for (ClassDec classDec : classes) {
            if (classDec != null) {
                return Optional.of(classDec);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Statements: " + statements.toString();
    }

    public static final class Visitor extends BaseV<Scope> {

        public Visitor(Scope scope) {
            super(scope);
        }

        @Override
        public Optional<Scope> visitScope(BulletLangParser.ScopeContext ctx) {
            Scope scope = new Visitor(this.scope)
                    .visit(ctx.scope())
                    .orElseGet(() -> new Scope(ctx));
            Optional<Statement> statement = new Statement.Visitor(scope)
                    .visit(ctx.statement());
            statement.ifPresent(scope.statements::add);

            scope.parentScope = this.scope;
            return Optional.of(scope);
        }

    }

}
