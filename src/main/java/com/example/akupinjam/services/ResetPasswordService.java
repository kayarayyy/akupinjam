package com.example.akupinjam.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.akupinjam.exceptions.ResourceNotFoundException;
import com.example.akupinjam.models.ResetPassword;
import com.example.akupinjam.models.User;
import com.example.akupinjam.repositories.ResetPasswordRepository;

import jakarta.transaction.Transactional;

@Service
public class ResetPasswordService {
    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    public List<ResetPassword> getAllResetPasswordToken() {
        List<ResetPassword> listResetPassword = resetPasswordRepository.findAll();

        return listResetPassword;
    }

    @Transactional
    public void getResetPasswordLink(String email) {
        User user = userService.getUserByEmail(email);

        // Mengecek apakah sudah ada permintaan reset password yang masih berlaku
        Optional<ResetPassword> existingReset = resetPasswordRepository.findByUserEmailAndExpiredAtAfter(email,
                LocalDateTime.now());

        ResetPassword resetPassword;
        if (existingReset.isPresent()) {
            System.out.println("masuk");
            // Jika sudah ada permintaan reset yang masih berlaku, perbarui expired time
            resetPassword = existingReset.get();
            resetPassword.setExpiredAt(LocalDateTime.now().plusHours(24));
        } else {
            System.out.println("masuk");
            // Jika belum ada, buat reset password baru
            resetPassword = new ResetPassword();
            resetPassword.setUser(user);
            resetPassword.setExpiredAt(LocalDateTime.now().plusHours(24));
        }

        resetPassword = resetPasswordRepository.save(resetPassword);
        sendResetPasswordEmail(user, resetPassword);
    }

    @Transactional
    public void setNewPasswordByResetPasswordEmail(String token, String email, String newPassword) {
        UUID resetPasswordId = UUID.fromString(token);

        // Cari data reset password berdasarkan ID (token) dan email
        ResetPassword resetPassword = resetPasswordRepository.findByIdAndUserEmail(resetPasswordId, email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        // Cek apakah sudah expired
        if (resetPassword.getExpiredAt().isBefore(LocalDateTime.now())) {
            resetPasswordRepository.delete(resetPassword); // Hapus token yang expired
            throw new IllegalStateException("Token sudah kedaluwarsa!"); // Gagal karena token sudah kedaluwarsa
        }

        // Jika valid, update password user
        User user = resetPassword.getUser(); // Pastikan password di-hash
        user.setPassword(newPassword);
        userService.updateUser(user.getId().toString(), user);

        // Hapus token setelah berhasil digunakan
        resetPasswordRepository.delete(resetPassword);
    }

    public void deleteResetPasswordToken(UUID id) {
        resetPasswordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));

        resetPasswordRepository.deleteById(id);
    }

    private void sendResetPasswordEmail(User user, ResetPassword resetPassword) {
        String subject = "Reset Password - AKuPinjam";

        String body = generateRegistrationEmailBody(user.getName(), user.getEmail(), resetPassword.getId().toString());
        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private String generateRegistrationEmailBody(String name, String email, String id) {
        return String.format(
                "Akun Anda meminta reset password di AKuPinjam.\n\n" +
                        "Abaikan jika ini bukan Anda,\n\n" +
                        "Berikut adalah detail akun Anda:\n" +
                        "Nama: " + name + "\n" +
                        "Email: " + email + "\n\n" +
                        "Klik link di bawah untuk mengatur ulang password Anda:\n\n" +
                        generateResetLink(id) + "\n\n" +
                        "Link ini hanya berlaku selama 24 jam.\n\n" +
                        "Terima kasih,\n" +
                        "Tim AKuPinjam");
    }

    private String generateResetLink(String id) {
        String baseUrl = "https://app.akupinjam.com/reset-password/"; // Sesuaikan dengan domain frontend
        return baseUrl + id.toString();
    }
}
