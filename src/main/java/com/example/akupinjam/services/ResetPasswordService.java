package com.example.akupinjam.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.akupinjam.models.ResetPassword;
import com.example.akupinjam.repositories.ResetPasswordRepository;

@Service
public class ResetPasswordService {
    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    public List<ResetPassword> getAllResetPassword(){
        List<ResetPassword> listResetPassword = resetPasswordRepository.findAll();

        return listResetPassword;
    }
}
