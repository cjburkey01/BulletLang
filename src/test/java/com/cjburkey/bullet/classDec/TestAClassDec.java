package com.cjburkey.bullet.classDec;

import com.cjburkey.bullet.TestHelper;
import com.cjburkey.bullet.parser.program.AProgram;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by CJ Burkey on 2018/12/01
 */
@SuppressWarnings("unused")
public class TestAClassDec {
    
    @Test
    public void testEmptyClass() {
        String identifier = "ExampleClass";
        String source = "class " + identifier + " { }";
        
        AProgram program = TestHelper.getProgram(source);
        assert(program.programIn.isPresent());
        assertEquals(program.programIn.get().classes.size(), 1);
        assertFalse(program.programIn.get().classes.get(0).isValue);
        assertEquals(program.programIn.get().classes.get(0).identifier, identifier);
        assertFalse(program.programIn.get().classes.get(0).types.isPresent());
        assertEquals(program.programIn.get().classes.get(0).classMembers.variableDecs.size(), 0);
        assertEquals(program.programIn.get().classes.get(0).classMembers.functionDecs.size(), 0);
    }
    
    @Test
    public void testConstructorInClass() {
        String identifier = "ExampleClass";
        String source = "class " + identifier + " { def _() {  } }";
        
        AProgram program = TestHelper.getProgram(source);
        assert(program.programIn.isPresent());
        assertEquals(program.programIn.get().classes.size(), 1);
        assertFalse(program.programIn.get().classes.get(0).isValue);
        assertEquals(program.programIn.get().classes.get(0).identifier, identifier);
        assertFalse(program.programIn.get().classes.get(0).types.isPresent());
        assertEquals(program.programIn.get().classes.get(0).classMembers.variableDecs.size(), 0);
        assertEquals(program.programIn.get().classes.get(0).classMembers.functionDecs.size(), 1);
        assert(program.programIn.get().classes.get(0).classMembers.functionDecs.get(0).isConstructor);
        assertFalse(program.programIn.get().classes.get(0).classMembers.functionDecs.get(0).arguments.isPresent());
    }
    
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void testMethodInClass() {
        String identifier1 = "ExampleClass";
        String identifier2 = "exampleMethod";
        String source = "class " + identifier1 + " { def " + identifier2 + "() { } }";
        
        AProgram program = TestHelper.getProgram(source);
        assert(program.programIn.isPresent());
        assertEquals(program.programIn.get().classes.size(), 1);
        assertFalse(program.programIn.get().classes.get(0).isValue);
        assertEquals(program.programIn.get().classes.get(0).identifier, identifier1);
        assertFalse(program.programIn.get().classes.get(0).types.isPresent());
        assertEquals(program.programIn.get().classes.get(0).classMembers.variableDecs.size(), 0);
        assertEquals(program.programIn.get().classes.get(0).classMembers.functionDecs.size(), 1);
        assert(program.programIn.get().classes.get(0).classMembers.functionDecs.get(0).identifier.isPresent());
        assertEquals(program.programIn.get().classes.get(0).classMembers.functionDecs.get(0).identifier.get(), identifier2);
        assertFalse(program.programIn.get().classes.get(0).classMembers.functionDecs.get(0).arguments.isPresent());
    }
    
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void testMethodsInClass() {
        String identifier1 = "ExampleClass";
        String identifier2 = "exampleMethod1";
        String identifier3 = "exampleMethod2";
        String source = "class " + identifier1 + " { def " + identifier2 + "() { } def " + identifier3 + "() { } }";
        
        AProgram program = TestHelper.getProgram(source);
        assert(program.programIn.isPresent());
        assertEquals(program.programIn.get().classes.size(), 1);
        assertFalse(program.programIn.get().classes.get(0).isValue);
        assertEquals(program.programIn.get().classes.get(0).identifier, identifier1);
        assertFalse(program.programIn.get().classes.get(0).types.isPresent());
        assertEquals(program.programIn.get().classes.get(0).classMembers.variableDecs.size(), 0);
        assertEquals(program.programIn.get().classes.get(0).classMembers.functionDecs.size(), 2);
        assert(program.programIn.get().classes.get(0).classMembers.functionDecs.get(0).identifier.isPresent());
        assertEquals(program.programIn.get().classes.get(0).classMembers.functionDecs.get(0).identifier.get(), identifier2);
        assertFalse(program.programIn.get().classes.get(0).classMembers.functionDecs.get(0).arguments.isPresent());
        assert(program.programIn.get().classes.get(0).classMembers.functionDecs.get(1).identifier.isPresent());
        assertEquals(program.programIn.get().classes.get(0).classMembers.functionDecs.get(1).identifier.get(), identifier3);
        assertFalse(program.programIn.get().classes.get(0).classMembers.functionDecs.get(1).arguments.isPresent());
    }
    
}
