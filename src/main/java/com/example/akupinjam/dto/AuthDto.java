package com.example.akupinjam.dto;


import com.example.akupinjam.models.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {

    private String email;
    private String name;
    private Role role;
    @JsonProperty("is_active")
    private boolean isActive;
    private String token;
}
