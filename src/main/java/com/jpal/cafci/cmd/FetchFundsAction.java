package com.jpal.cafci.cmd;

import lombok.Getter;

public record FetchFundsAction() implements Action {

    @Getter(lazy = true)
    private static final FetchFundsAction instance = new FetchFundsAction();

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }

}
