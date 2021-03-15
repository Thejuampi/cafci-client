package com.jpal.cafci.cmd;

import lombok.Value;

@Value
public class FetchFundsAction implements Action {

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }

}