
package com.example.akupinjam.dto;

import java.util.UUID;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureDto {
    private UUID id;
    private String name;
}