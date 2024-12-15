package org.zjubs.pricecomwebbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Good {
    private Integer id;
    private String description;
    private Double price;
    private String shopName;
    private String img;
    private String category;
    private LocalDateTime time;
    private String queryName;
    private String detailUrl;
}
