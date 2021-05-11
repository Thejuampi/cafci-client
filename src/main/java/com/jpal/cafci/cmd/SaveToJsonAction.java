package com.jpal.cafci.cmd;

public class SaveToJsonAction implements Action {

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }

}
