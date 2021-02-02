package com.jpal.cafci.client;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class FundClass {

    @NonNull String id;
    @NonNull String name;

}
