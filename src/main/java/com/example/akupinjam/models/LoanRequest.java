package com.example.akupinjam.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String amount;

    @ManyToOne
    @JoinColumn(name = "branch", referencedColumnName = "city")
    private Branch branch;
    
    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "customer_email", referencedColumnName = "email")
    private User customer;
    
    @ManyToOne
    @JoinColumn(name = "marketing_email", referencedColumnName = "email")
    private User marketing;

    @Column(nullable = true)
    private Boolean marketingApprove;
    
    @ManyToOne
    @JoinColumn(name = "branch_manager_email", referencedColumnName = "email")
    private User branchManager;

    @Column(nullable = true)
    private Boolean branchManagerApprove;
    
    @ManyToOne
    @JoinColumn(name = "back_office_email", referencedColumnName = "email")
    private User backOffice;
    
    @Column(nullable = true)
    private Boolean backOfficeApproveDisburse;

}
