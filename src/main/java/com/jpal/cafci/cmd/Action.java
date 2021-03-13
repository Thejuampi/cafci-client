package com.jpal.cafci.cmd;

public interface Action {

    void execute(ActionVisitor visitor);

}
