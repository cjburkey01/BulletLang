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

    public static final class BasicTypes {

        public static final RawType INT32 = new RawType("Int32");
        public static final RawType INT64 = new RawType("Int64");
        public static final RawType FLOAT32 = new RawType("Float32");
        public static final RawType FLOAT64 = new RawType("Float64");
        public static final RawType STRING = new RawType("String");
        public static final RawType BOOLEAN = new RawType("Boolean");
        public static final RawType VOID = new RawType("Void");
        public static final RawType NULL = new RawType("Null");

    }

}
