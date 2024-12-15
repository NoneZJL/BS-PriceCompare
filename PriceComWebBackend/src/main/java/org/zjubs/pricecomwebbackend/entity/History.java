package org.zjubs.pricecomwebbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private Integer id;
    private Integer userId;
    private String content;
    private LocalDateTime time;
}
