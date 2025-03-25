package com.example.akupinjam.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loan_request")
public class LoanRequest {
    @Id
    @Column(length = 20)
    private String id;

    @Column(nullable = false)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "marketing_id", referencedColumnName = "id")
    private User marketing;

    @Column(nullable = true)
    private Boolean marketingApprove;
    
    @ManyToOne
    @JoinColumn(name = "branch_manager_id", referencedColumnName = "id")
    private User branchManager;

    @Column(nullable = true)
    private Boolean branchManagerApprove;
    
    @ManyToOne
    @JoinColumn(name = "back_office_id", referencedColumnName = "id")
    private User backOffice;
    
    @Column(nullable = true)
    private Boolean backOfficeApprove;

    @PrePersist
    public void generateUniqueId() {
        String timeStamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HHmmss"));
        this.id = "LR" + customer.getId() + timeStamp;
    }
}
