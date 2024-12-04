package org.vaskozov.lab4.lib;

import lombok.*;

@Data
@AllArgsConstructor
public class RequestValidationError {
    private String component;
    private String message;
}
