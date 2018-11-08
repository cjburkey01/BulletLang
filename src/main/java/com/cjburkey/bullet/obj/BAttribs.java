package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.obj.classdef.BVisibility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/07
 */
@SuppressWarnings("WeakerAccess")
public class BAttribs {
    
    public final List<String> attribs = new ArrayList<>();
    
    public BVisibility getVisibility() {
        if (attribs.contains("Private")) {
            return BVisibility.PRIVATE;
        }
        if (attribs.contains("Restrict")) {
            return BVisibility.RESTRICT;
        }
        return null;
    }
    
    public String toString() {
        return String.format("Attribs (%s): %s", attribs.size(), Arrays.toString(attribs.toArray(new String[0])));
    }
    
}
