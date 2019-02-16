package com.cjburkey.bullet;

import java.util.function.Consumer;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by CJ Burkey on 2018/11/21
 */
@SuppressWarnings("WeakerAccess")
public class BulletError {

    public final String printText;
    public final ParserRuleContext ctx;

    public BulletError(String printText, ParserRuleContext ctx) {
        this.printText = printText;
        this.ctx = ctx;
    }

    public static BulletError format(ParserRuleContext ctx, String format, Object... data) {
        return new BulletError(String.format(format, data), ctx);
    }

    public void print(Consumer<String> log) {
        log.accept(String.format("%s on line %s at character %s", printText, ctx.start.getLine(), ctx.start.getCharPositionInLine() + 1));
    }

}
