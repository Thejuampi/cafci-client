package com.jpal.cafci.cmd;

public record ReadFileAction() implements Action {

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }

}
