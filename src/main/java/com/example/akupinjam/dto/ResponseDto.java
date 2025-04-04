package com.example.akupinjam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    @JsonProperty("status_code")
    private int statusCode;
    private String status;
    private String message;
    private Object data;
}
