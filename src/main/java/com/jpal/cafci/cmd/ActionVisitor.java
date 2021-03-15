package com.jpal.cafci.cmd;

public interface ActionVisitor {

    void visit(FetchFundsAction __);

    void visit(ReadFileAction __);

    void visit(FundAction fundAction);

}