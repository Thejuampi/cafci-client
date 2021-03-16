package com.jpal.cafci.shared;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.function.Consumer;

public abstract class Result<OK, ERROR> {

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

    @Value
    @EqualsAndHashCode(callSuper = false)
    static class Ok<OK, ERROR>
            extends Result<OK, ERROR> {

        OK value;

        @Override
        public void continued(Consumer<OK> okConsumer,
                              Consumer<ERROR> errorConsumer) {
            okConsumer.accept(value);
        }
    }

    @Value
    @EqualsAndHashCode(callSuper = false)
    static class Error<OK, ERROR>
            extends Result<OK, ERROR> {

        ERROR error;

        @Override
        public void continued(Consumer<OK> okConsumer,
                              Consumer<ERROR> errorConsumer) {
            errorConsumer.accept(error);
        }
    }

}
