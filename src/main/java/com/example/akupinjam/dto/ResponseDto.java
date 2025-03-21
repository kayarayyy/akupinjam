package com.example.akupinjam.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private String status;
    private String message;
    private Object data;

}
