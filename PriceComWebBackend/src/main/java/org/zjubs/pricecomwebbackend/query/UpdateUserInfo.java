package org.zjubs.pricecomwebbackend.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfo {
    private String phone;
    private String address;
}
