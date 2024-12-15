package org.vaskozov.lab4.lib;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtError {
    private final String message;
    private int status;
}
