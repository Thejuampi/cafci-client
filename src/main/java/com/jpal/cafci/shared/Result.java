package com.jpal.cafci.shared;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Result<OK, ERROR> {

    public abstract OK ok();
    public abstract ERROR error();
    public abstract boolean isError();
    public abstract boolean isOk();

    @SuppressWarnings("unchecked")
    public final <OK2> Result<OK2, ERROR> cast() {
        return (Result<OK2, ERROR>) this;
    }

    public static <OK, ERROR> Result<OK, ERROR> ok(OK ok) {
        return new Ok<>(ok);
    }

    public static <OK, ERROR> Result<OK, ERROR> error(ERROR error) {
        return new Error<>(error);
    }

    public static <OK> Result<OK, String> error(String format, Object... args) {
        return new LazyStringError<>(() -> format.formatted(args));
    }

    public abstract void continued(
            Consumer<OK> okConsumer,
            Consumer<ERROR> errorConsumer
    );

    @Value
    @EqualsAndHashCode(callSuper = false)
    static class Ok<OK, ERROR>
            extends Result<OK, ERROR> {

        OK ok;

        @Override
        public ERROR error() { throw new IllegalStateException("this is an ok!"); }

        @Override
        public void continued(Consumer<OK> okConsumer,
                              Consumer<ERROR> errorConsumer) {
            okConsumer.accept(ok);
        }

        @Override
        public boolean isOk() { return true; }

        @Override
        public boolean isError() { return false; }
    }

    @Value
    @EqualsAndHashCode(callSuper = false)
    static class Error<OK, ERROR>
            extends Result<OK, ERROR> {

        ERROR error;

        @Override
        public OK ok() { throw new IllegalStateException("this is an error!: " + error); }

        @Override
        public void continued(Consumer<OK> okConsumer,
                              Consumer<ERROR> errorConsumer) {
            errorConsumer.accept(error);
        }

        @Override
        public boolean isOk() { return false; }

        @Override
        public boolean isError() { return true; }
    }

    @Value
    @EqualsAndHashCode(callSuper = false)
    private static class LazyStringError <OK>
            extends Result<OK, String> {

        Supplier<String> supplier;

        @Override
        public OK ok() { throw new IllegalStateException("this is an error!: " + error()); }

        @Override
        public String error() { return supplier.get(); }

        @Override
        public boolean isError() { return true; }

        @Override
        public void continued(Consumer<OK> ignored,
                              Consumer<String> errorConsumer) {
            errorConsumer.accept(this.supplier.get());
        }

        @Override
        public boolean isOk() { return false; }

    }
}
