package com.cjburkey.bullet.visitor.struct;

import com.cjburkey.bullet.obj.BFunction;
import com.cjburkey.bullet.obj.classdef.BClass;
import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class ProgramIn {
    
    public final List<BFunction> functions = new ArrayList<>();
    public final List<BClass> classes = new ArrayList<>();
    public final List<BStatement> statements = new ArrayList<>();
    
}
