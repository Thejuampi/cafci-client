package com.jpal.cafci.cmd;

public class StopAction implements Action {
    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }
}
