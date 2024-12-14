package org.vaskozov.lab4.lib;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthorizationInfo {
    private Login login;
    private Password password;
}
