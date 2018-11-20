package com.cjburkey.bullet.parser.classDec;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.parser.ABase;
import com.cjburkey.bullet.parser.AVariableDec;
import com.cjburkey.bullet.parser.function.AFunctionDec;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/20
 */
public class AClassMembers extends ABase {
    
    public final List<AVariableDec> variableDecs = new ArrayList<>();
    public final List<AFunctionDec> functionDecs = new ArrayList<>();
    
    public AClassMembers(BulletParser.ClassMembersContext ctx) {
        super(ctx);
    }
    
}
