package com.cjburkey.bullet.visitor.struct;

import com.cjburkey.bullet.obj.BNamespace;
import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class ProgramIn {
    
    public final List<BNamespace> namespaces = new ArrayList<>();
    public final List<BStatement> statements = new ArrayList<>();
    public final List<Content> contents = new ArrayList<>();
    
}
