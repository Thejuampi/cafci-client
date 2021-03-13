package com.jpal.cafci.shared;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("unused")
@Retention(RetentionPolicy.SOURCE)
public @interface Impure {

    String cause();

}
