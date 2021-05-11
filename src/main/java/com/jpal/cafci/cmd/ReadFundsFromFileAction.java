package com.jpal.cafci.cmd;

public class ReadFundsFromFileAction implements Action {
    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }
}
