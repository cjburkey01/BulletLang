package com.cjburkey.bullet.parser;

import java.util.Objects;

/**
 * Created by CJ Burkey on 2019/02/16
 */
public class RawType {

    private String type;

    public RawType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawType rawType = (RawType) o;
        return Objects.equals(type, rawType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

}
