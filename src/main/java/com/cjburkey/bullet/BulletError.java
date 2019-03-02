package com.cjburkey.bullet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Consumer;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/21
 */
public class BulletError {

    private static final ObjectArrayList<BulletError> errors = new ObjectArrayList<>();

    private final String printText;
    private final ParserRuleContext ctx;

    private BulletError(String printText, ParserRuleContext ctx) {
        this.printText = printText;
        this.ctx = ctx;
    }

    public String toString() {
        return String.format("%s on line %s at character %s", printText, ctx.start.getLine(), ctx.start.getCharPositionInLine());
    }

    private void print(Consumer<String> log) {
        log.accept(toString());
    }

    private static BulletError format(ParserRuleContext ctx, String format, Object... data) {
        return new BulletError(String.format(format, data), ctx);
    }

    public static void queueError(ParserRuleContext ctx, String message, Object... data) {
        errors.add(format(ctx, message, data));
    }

    static boolean dumpAndClearErrors() {
        if (errors.size() <= 0) return false;
        errors.forEach(error -> error.print(Log::error));
        return quietDumpErrors();
    }

    private static boolean quietDumpErrors() {
        if (errors.size() <= 0) return false;
        errors.clear();
        return true;
    }

}
