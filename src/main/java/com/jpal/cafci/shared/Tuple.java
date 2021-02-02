package com.jpal.cafci.shared;

import lombok.EqualsAndHashCode;
import lombok.Value;

public abstract class Tuple {

    public static <T1> Tuple1<T1> tuple(T1 t1) {
        return new Tuple1<>(t1);
    }

    public static <T1, T2> Tuple2<T1, T2> tuple(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    public static class Tuple1<T1> extends Tuple {
        T1 t1;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    public static class Tuple2<T1, T2> extends Tuple {
        T1 t1;
        T2 t2;
    }
}
