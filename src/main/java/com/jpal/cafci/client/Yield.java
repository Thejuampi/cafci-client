package com.jpal.cafci.client;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class Yield {

    LocalDate from;
    LocalDate to;
    double value;
    double accumulated;
    double direct;
    double tna;

}
