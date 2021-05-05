package com.jpal.cafci.cmd;

public record FetchFundsAction() implements Action {

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }

}
