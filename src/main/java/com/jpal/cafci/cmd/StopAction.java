package com.jpal.cafci.cmd;

import lombok.Value;

@Value
public class StopAction implements Action {
    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }
}
