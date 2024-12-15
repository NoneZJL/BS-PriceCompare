package org.zjubs.pricecomwebbackend.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyPassword {
    private String email;
    private String password;
    private String newEmailCode;
    private String lastEmailCode;
}
