package org.zjubs.pricecomwebbackend.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Remainder {
    private Integer id;
    private Integer userId;
    private String description;
    private Double price;
    private String img;
    private String detailUrl;
    private String from;
}
