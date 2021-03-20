package com.jpal.cafci.shared;

import lombok.Value;

import java.util.Map;

@Value
public class Args {

    String action;
    Map<String, String> args;

}
