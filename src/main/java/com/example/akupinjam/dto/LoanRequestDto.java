package com.example.akupinjam.dto;

import com.example.akupinjam.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequestDto {
    private String id;
    private String amount;
    private User customer;
}
