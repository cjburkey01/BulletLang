package com.cjburkey.bullet.obj.statement;

import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BBase;
import java.util.Objects;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BArgument extends BBase {
    
    public final String name;
    public final String type;
    
    public BArgument(String name, String type, BulletParser.ArgumentContext ctx) {
        super(ctx);
        
        this.name = name;
        this.type = type;
    }
    
    public String toString() {
        return String.format("[%s] as [%s]", name, type == null ? '?' : type);
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BArgument bArgument = (BArgument) o;
        return Objects.equals(name, bArgument.name) && Objects.equals(type, bArgument.type);
    }
    
    public int hashCode() {
        return Objects.hash(name, type);
    }
    
}
