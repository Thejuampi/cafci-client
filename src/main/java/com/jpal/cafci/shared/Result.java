package com.jpal.cafci.shared;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.function.Consumer;

public abstract class Result<OK, ERROR> {

    public abstract OK ok();
    public abstract ERROR error();

    public static <OK, ERROR> Result<OK, ERROR> ok(OK ok) {
        return new Ok<>(ok);
    }

    public static <OK, ERROR> Result<OK, ERROR> error(ERROR error) {
        return new Error<>(error);
    }

    public abstract void continued(
            Consumer<OK> okConsumer,
            Consumer<ERROR> errorConsumer
    );

    public abstract boolean isError();

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
        public boolean isError() { return true; }
    }

}
