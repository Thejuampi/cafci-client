package com.jpal.cafci.cmd;

import lombok.Value;

@Value
public class FundAction implements Action {

    String fund;

    @Override
    public void execute(ActionVisitor visitor) {
        visitor.visit(this);
    }
}
