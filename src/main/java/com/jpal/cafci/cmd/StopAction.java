package com.jpal.cafci.cmd;

import lombok.Getter;

public record StopAction() implements Action {

    @Getter(lazy = true)
    private static final StopAction instance = new StopAction();

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }

}
