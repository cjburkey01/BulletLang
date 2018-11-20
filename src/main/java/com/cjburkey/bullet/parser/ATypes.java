package com.cjburkey.bullet.parser;

import com.cjburkey.bullet.antlr.BulletParser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class ATypes extends ABase {
    
    public final List<String> types = new ArrayList<>();
    
    public ATypes(BulletParser.TypesContext ctx) {
        super(ctx);
    }
    
}
