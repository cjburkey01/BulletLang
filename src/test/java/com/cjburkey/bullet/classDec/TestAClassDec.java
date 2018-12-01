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
        assertEquals(program.programIn.get().classes.get(0).identifier, identifier);
        assertFalse(program.programIn.get().classes.get(0).types.isPresent());
        assertEquals(program.programIn.get().classes.get(0).classMembers.functionDecs.size(), 0);
        assertEquals(program.programIn.get().classes.get(0).classMembers.variableDecs.size(), 0);
    }
    
}
