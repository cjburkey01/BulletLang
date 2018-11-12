package com.cjburkey.bullet.compiler;

import com.cjburkey.bullet.obj.BFunction;
import com.cjburkey.bullet.obj.BNamespace;
import com.cjburkey.bullet.obj.BProgram;
import com.cjburkey.bullet.obj.classdef.BClass;
import com.cjburkey.bullet.obj.classdef.IBClassMember;
import com.cjburkey.bullet.obj.statement.BArgument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cjburkey.bullet.Log.*;

/**
 * Created by CJ Burkey on 2018/11/09
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class Compiler {
    
    public static boolean compile(BProgram program) {
        if (!verify(program)) {
            return false;
        }
        return false;
    }
    
    private static boolean verify(BProgram program) {
        if (!Namespace.verifyNamespaces(program.namespaces)) {
            return false;
        }
        if (!Function.verifyFunctions(program.functions)) {
            return false;
        }
        if (!Class.verifyClasses(program.classes)) {
            return false;
        }
        return true;
    }
    
    private static final class Namespace {
        private static boolean verifyNamespaces(List<BNamespace> namespaces) {
            Map<String, BNamespace> namespaceMap = new HashMap<>();
            
            // Merge duplicate namespaces
            for (BNamespace namespace : namespaces) {
                if (namespaceMap.containsKey(namespace.name)) {
                    debug("Found addon for namespace: [{}]", namespace.name);
                    BNamespace at = namespaceMap.get(namespace.name);
                    at.functions.addAll(namespace.functions);
                    at.classes.addAll(namespace.classes);
                } else {
                    namespaceMap.put(namespace.name, namespace);
                }
            }
            namespaces.clear();
            
            // Verify and re-add valid namespaces
            for (BNamespace namespace : namespaceMap.values()) {
                if (!verifyNamespace(namespace)) {
                    return false;
                }
                namespaces.add(namespace);
            }
            return true;
        }
        
        private static boolean verifyNamespace(BNamespace namespace) {
            if (!Function.verifyFunctions(namespace.functions)) {
                return false;
            }
            if (!Class.verifyClasses(namespace.classes)) {
                return false;
            }
            return true;
        }
    }
    
    private static final class Function {
        private static boolean verifyFunctions(List<BFunction> functions) {
            Map<String, List<BFunction>> functionMap = new HashMap<>();
            
            // Fail upon duplicate function
            for (BFunction function : functions) {
                List<BFunction> at;
                if (functionMap.containsKey(function.name)) {
                    at = functionMap.get(function.name);
                } else {
                    at = new ArrayList<>();
                    functionMap.put(function.name, at);
                }
                for (BFunction atIn : at) {
                    List<String> argTypes = new ArrayList<>();
                    List<String> argTypesAt = new ArrayList<>();
                    for (BArgument arg : function.arguments) {
                        argTypes.add(arg.type);
                    }
                    for (BArgument arg : atIn.arguments) {
                        argTypesAt.add(arg.type);
                    }
                    if (argTypes.equals(argTypesAt)) {
                        error("Duplicate function: [{}] with arguments of types: {}", function.name, Arrays.toString(argTypes.toArray(new String[0])));
                        return false;
                    }
                }
                at.add(function);
            }
            
            // Verify functions
            for (List<BFunction> functionss : functionMap.values()) {
                for (BFunction function : functionss) {
                    if (!verifyFunction(function)) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        // TODO: VERIFY FUNCTIONS
        private static boolean verifyFunction(BFunction function) {
            return false;
        }
    }
    
    private static final class Class {
        private static boolean verifyClasses(List<BClass> classes) {
            Map<String, BClass> classMap = new HashMap<>();
            
            // Merge duplicate classes
            for (BClass classDef : classes) {
                if (classMap.containsKey(classDef.name)) {
                    debug("Found addon for namespace: [{}]", classDef.name);
                    BClass at = classMap.get(classDef.name);
                    at.members.addAll(classDef.members);
                } else {
                    classMap.put(classDef.name, classDef);
                }
            }
            classes.clear();
            
            // Verify and re-add valid classes
            for (BClass classDef : classMap.values()) {
                if (!verifyClass(classDef)) {
                    return false;
                }
                classes.add(classDef);
            }
            return true;
        }
        
        private static boolean verifyClass(BClass classDef) {
            return verifyClassMembers(classDef.members);
        }
        
        // TODO: VERIFY MEMBERS
        private static boolean verifyClassMembers(List<IBClassMember> members) {
            return false;
        }
    }
    
}
