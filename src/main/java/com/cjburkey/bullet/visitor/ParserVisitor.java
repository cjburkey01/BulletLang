package com.cjburkey.bullet.visitor;

import com.cjburkey.bullet.antlr.BulletBaseVisitor;
import com.cjburkey.bullet.antlr.BulletParser;
import java.util.Optional;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by CJ Burkey on 2018/11/03
 */
@SuppressWarnings("WeakerAccess")
public class ParserVisitor {
    
    // Visitor instances (to prevent repeat instantiation)
    public static final ProgramVisitor _programVisitor = new ProgramVisitor();
    
    // Calls the program visitor on the supplied program context
    public static Optional<Void> parseProgram(BulletParser.ProgramContext ctx) {
        return _programVisitor.visit(ctx);
    }
    
    // Visit the outer program, this starts the entire parse process
    public static final class ProgramVisitor extends B<Void> {
        public Optional<Void> visitProgram(BulletParser.ProgramContext ctx) {
            return Optional.empty();
        }
    }
    
    // The base, will allow handling of actions during any visit call
    // Could be used to detect errors, etc. For now, it just enforces the Optional system
    public static class B<T> extends BulletBaseVisitor<Optional<T>> {
        public Optional<T> visit(final ParseTree tree) {
            if (tree == null) {
                return Optional.empty();
            }
            return tree.accept(this);
        }
    }
    
}
