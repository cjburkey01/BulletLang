package com.cjburkey.bullet.compiler;

import com.cjburkey.bullet.Log;
import com.cjburkey.bullet.compiler.error.ErrorCompile;
import com.cjburkey.bullet.compiler.error.ErrorDuplicateFunction;
import com.cjburkey.bullet.compiler.error.ErrorInvalidClassMember;
import com.cjburkey.bullet.obj.BFunction;
import com.cjburkey.bullet.obj.BNamespace;
import com.cjburkey.bullet.obj.BProgram;
import com.cjburkey.bullet.obj.classdef.BClass;
import com.cjburkey.bullet.obj.classdef.IBClassMember;
import com.cjburkey.bullet.obj.scope.BScope;
import com.cjburkey.bullet.obj.scope.IBScopeContainer;
import com.cjburkey.bullet.obj.statement.BArgument;
import com.cjburkey.bullet.obj.statement.BStatement;
import com.cjburkey.bullet.obj.statement.BVariable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.cjburkey.bullet.Log.*;

/**
 * Created by CJ Burkey on 2018/11/09
 */
@SuppressWarnings({"WeakerAccess", "RedundantIfStatement"})
public class Compiler {
    
    private final BProgram program;
    public final List<ErrorCompile> errors = new ArrayList<>();
    
    public Compiler(BProgram program) {
        this.program = program;
    }
    
    public boolean compile() {
        if (!verify()) {
            return true;
        }
        return false;
    }
    
    private boolean verify() {
        if (!clean()) {
            return false;
        }
        return true;
    }
    
    private boolean trueOnError() {
        if (errors.size() > 0) {
            error("Errors were encountered during compilation:");
            for (ErrorCompile error : errors) {
                error("  {}", error.message);
            }
            return true;
        }
        return false;
    }
    
    // -- CLEANING -- //
    // Preceeds compilation error detection, though errors may be found during cleaning.
    // Prepares program to be verified
    
    private boolean clean() {
        cleanNamespaces(program.namespaces);
        if (trueOnError()) return false;
        
        cleanFunctions(program, program, program.functions);
        if (trueOnError()) return false;
        
        cleanClasses(program, program, program.classes);
        if (trueOnError()) return false;
        
        cleanScope(program, program, program.scope);
        if (trueOnError()) return false;
        return true;
    }
    
    private void cleanNamespaces(List<BNamespace> parentNamespaces) {
        Map<String, BNamespace> namespaces = new LinkedHashMap<>();
        
        // Merge namespaces of the same name
        for (BNamespace namespace : parentNamespaces) {
            if (namespaces.containsKey(namespace.name)) {
                debug("Merging namespace: [{}]", namespace.name);
                BNamespace at = namespaces.get(namespace.name);
                at.functions.addAll(namespace.functions);
                at.classes.addAll(namespace.classes);
            } else {
                namespaces.put(namespace.name, namespace);
            }
        }
        
        // Make sure namespaces' members have correct namespace and parent
        for (BNamespace namespace : namespaces.values()) {
            namespace.setNamespace(program);
            namespace.setParent(program);
            for (BFunction function : namespace.functions) {
                function.setNamespace(namespace);
                function.setParent(namespace);
            }
            for (BClass classDef : namespace.classes) {
                classDef.setNamespace(namespace);
                classDef.setParent(namespace);
            }
        }
        
        // Re-add merged namespaces 
        parentNamespaces.clear();
        parentNamespaces.addAll(namespaces.values());
        
        // Clean child functions and classes
        for (BNamespace namespace : parentNamespaces) {
            cleanFunctions(namespace, namespace, namespace.functions);
            cleanClasses(namespace, namespace, namespace.classes);
        }
    }
    
    private void cleanFunctions(IBScopeContainer parent, BNamespace namespace, List<BFunction> parentFunctions) {
        Map<String, BFunction> functions = new LinkedHashMap<>();
        
        // Check for functions with the same names and argument types
        for (BFunction function : parentFunctions) {
            if (functions.containsKey(function.name)) {
                List<String> argsA = new ArrayList<>();
                List<String> argsB = new ArrayList<>();
                for (BArgument argument : function.arguments) {
                    argsA.add(argument.type);
                }
                for (BArgument argument : functions.get(function.name).arguments) {
                    argsB.add(argument.type);
                }
                if (argsA.equals(argsB)) {
                    errors.add(new ErrorDuplicateFunction(function.name, argsA));
                }
            } else {
                functions.put(function.name, function);
            }
        }
        
        // Set namespaces and parents for functions
        for (BFunction function : functions.values()) {
            function.setParent(parent);
            function.setNamespace(namespace);
        }
        
        // Re-add valid for possible further compilation functions
        // (Might allow further error detection, don't verify already-errored functions)
        parentFunctions.clear();
        parentFunctions.addAll(functions.values());
    }
    
    private void cleanClasses(IBScopeContainer parent, BNamespace namespace, List<BClass> parentClasses) {
        Map<String, BClass> classes = new LinkedHashMap<>();
        
        // Merge classes of the same name
        for (BClass classDef : parentClasses) {
            if (classes.containsKey(classDef.name)) {
                debug("Merging class: [{}]", classDef.name);
                classes.get(classDef.name).members.addAll(classDef.members);
            } else {
                classes.put(classDef.name, classDef);
            }
        }
        
        // Make sure class' members have correct namespace and parent
        for (BClass classDef : classes.values()) {
            classDef.setParent(parent);
            classDef.setNamespace(namespace);
        }
        
        // Re-add merged classes
        parentClasses.clear();
        parentClasses.addAll(classes.values());
        
        // Clean child functions and variables
        for (BClass classDef : parentClasses) {
            List<BFunction> functions = new ArrayList<>();
            List<BVariable> variables = new ArrayList<>();
            for (IBClassMember member : classDef.members) {
                if (member instanceof BFunction) {
                    functions.add((BFunction) member);
                } else if (member instanceof BVariable) {
                    variables.add((BVariable) member);
                } else {
                    errors.add(new ErrorInvalidClassMember(member));
                }
            }
            cleanFunctions(classDef, namespace, functions);
            cleanVariables(classDef, namespace, variables);
        }
    }
    
    // TODO: THIS ISN'T WORKING FOR SOME REASON
    private void cleanVariables(IBScopeContainer parent, BNamespace namespace, List<BVariable> parentVariables) {
        Map<String, BVariable> variables = new LinkedHashMap<>();
        for (BVariable variable : parentVariables) {
            if (!variable.declaration) {
                errors.add(new ErrorCompile(String.format("Variable value assignment in class without declaration: [%s]", variable.name)));
            } else if (variables.containsKey(variable.name)) {
                // TODO: ALLOW VARIABLE SHADOWING
                errors.add(new ErrorCompile(String.format("Duplicate variable declaration: [%s]", variable.name)));
            } else {
                variables.put(variable.name, variable);
            }
        }
        
        for (BVariable variable : variables.values()) {
            variable.setParent(parent);
            variable.setNamespace(namespace);
        }
        
        parentVariables.clear();
        parentVariables.addAll(variables.values());
    }
    
    // TODO: DO MORE IN HERE
    private void cleanScope(IBScopeContainer parent, BNamespace namespace, BScope parentScope) {
        List<BVariable> variables = new ArrayList<>();
        
        for (BStatement statement : parentScope.statements) {
            if (statement instanceof BVariable) {
                variables.add((BVariable) statement);
            }
        }
        
        parentScope.setParent(parent);
        parentScope.setNamespace(namespace);
        
        cleanVariables(parent, namespace, variables);
    }
    
}
