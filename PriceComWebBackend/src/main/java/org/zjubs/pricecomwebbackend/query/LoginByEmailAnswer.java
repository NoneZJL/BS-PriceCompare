package org.zjubs.pricecomwebbackend.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginByEmailAnswer {
    private String token;
    private String username;
}
