package com.example.akupinjam.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.akupinjam.dto.ResponseDto;
import com.example.akupinjam.services.LoanRequestService;

@RestController
@RequestMapping("api/v1/loan-requests")
public class LoanRequestController {

    @Autowired
    LoanRequestService loanRequestService;

    @PostMapping
    public ResponseEntity<ResponseDto> createLoanRequest(@RequestBody Map<String, Object> payload) {

        ResponseDto responseDto = loanRequestService.createLoanRequest(payload);

        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);

    }
}
