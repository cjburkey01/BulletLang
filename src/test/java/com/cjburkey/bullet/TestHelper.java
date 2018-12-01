package com.cjburkey.bullet;

import com.cjburkey.bullet.parser.program.AProgram;
import com.cjburkey.bullet.visitor.ParserVisitor;
import java.util.Optional;

/**
 * Created by CJ Burkey on 2018/12/01
 */
public final class TestHelper {
    
    public static AProgram getProgram(String input) {
        Optional<AProgram> program = ParserVisitor.parseProgram(BulletLang.buildQuickDumpParser(input).program());
        assert(program.isPresent());
        program.get().settleChildren();
        return program.get();
    }
    
}
