package com.jpal.cafci.cmd;

public record StopAction() implements Action {

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }

}
