package com.backendLevelup.Backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;
    private String name;
    private Long price;
    private String imageName;
    private String description;
    private Double rating;
}
