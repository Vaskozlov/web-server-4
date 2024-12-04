package org.vaskozov.lab4.lib;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RequestParameters {
    private double x;
    private double y;
    private double r;
}
