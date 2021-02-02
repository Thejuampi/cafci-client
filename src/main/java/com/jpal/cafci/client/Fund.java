package com.jpal.cafci.client;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Fund {

    @NonNull String id; // this may be moved outside the class
    @NonNull String name;
    @ToString.Exclude String objective;
    @Builder.Default
    @NonNull List<FundClass> classes = List.of(); // this may be changed to a Map<String, FundClass>

}
