package com.jpal.cafci.cmd;

import lombok.Getter;

public record ReadFileAction() implements Action {

    @Getter(lazy = true)
    private static final ReadFileAction instance = new ReadFileAction();

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }

}
