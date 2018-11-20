package com.cjburkey.bullet.visitor;

import com.cjburkey.bullet.antlr.BulletBaseVisitor;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.program.AProgram;
import java.util.Optional;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class ParserVisitor {
    
    private static class ProgramVisitor extends B<AProgram> {
        public Optional<AProgram> visitProgram(BulletParser.ProgramContext ctx) {
            return Optional.empty();
        }
    }
    
    private static class B<T> extends BulletBaseVisitor<Optional<T>> {
        public Optional<T> visit(ParseTree tree) {
            if (tree == null) {
                return Optional.empty();
            }
            return tree.accept(this);
        }
    }
    
}
