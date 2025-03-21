package com.example.akupinjam.dto;

import com.example.akupinjam.models.Role;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

    private String email;
    private String nama;
    private Role role;
    private String alamat;
    private Integer nip;
}
