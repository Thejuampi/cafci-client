package com.jpal.cafci.shared;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

public abstract class Tuple {

    public abstract <T> T _1();
    public abstract <T> T _2();

    @SuppressWarnings("unused")
    public static <T1> Tuple tuple(T1 t1) {
        return new Tuple1<>(t1);
    }

    public static <T1, T2> Tuple tuple(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    @Value
    @EqualsAndHashCode(callSuper = false)
    @SuppressWarnings("unchecked")
    public static class Tuple1<T1> extends Tuple {
        @NonNull T1 t1;

        @Override
        public T1 _1() {
            return t1;
        }

        @Override
        public <T> T _2() {
            throw new UnsupportedOperationException("this is a T1");
        }
    }

    @Value
    @EqualsAndHashCode(callSuper = false)
    @SuppressWarnings("unchecked")
    public static class Tuple2<T1, T2> extends Tuple {
        @NonNull T1 t1;
        @NonNull T2 t2;

        @Override
        public T1 _1() {
            return t1;
        }

        @Override
        public T2 _2() {
            return t2;
        }
    }
}
