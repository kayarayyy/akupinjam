package com.example.akupinjam.services;

import com.example.akupinjam.dto.LoanRequestDto;
import com.example.akupinjam.exceptions.ResourceNotFoundException;
import com.example.akupinjam.models.Branch;
import com.example.akupinjam.models.LoanRequest;
import com.example.akupinjam.models.User;
import com.example.akupinjam.repositories.LoanRequestRepository;
import com.example.akupinjam.utils.JwtUtil;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

@Service
public class LoanRequestService {

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private BranchService branchService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public LoanRequestDto createLoanRequest(Map<String, Object> payload, String token) {
        LoanRequest loanRequest = new LoanRequest();

        loanRequest.setAmount(payload.get("amount").toString());

        double userLat = Double.parseDouble(payload.get("latitude").toString());
        double userLon = Double.parseDouble(payload.get("longitude").toString());
        loanRequest.setLatitude(userLat);
        loanRequest.setLongitude(userLon);
        
        
        // Set Customer (required)
        String email = jwtUtil.extractEmail(token);
        User customer = userService.getUserByEmail(email);
        loanRequest.setCustomer(customer);
        
        // Set Marketing (optional)
        if (payload.containsKey("refferal") && payload.get("refferal") != null) {
            User marketer = userService.getUserByRefferal(payload.get("refferal").toString());
            loanRequest.setMarketing(marketer);
            Branch branch = marketer.getBranch();
            loanRequest.setBranch(branch);
        } else {
            Branch nearestBranch = branchService.findNearestBranch(userLat, userLon);
            loanRequest.setBranch(nearestBranch);
        }

        // Set Branch Manager (optional)
        // if (payload.containsKey("branchManagerId")) {
        // userRepository.findById(UUID.fromString(payload.get("branchManagerId").toString()))
        // .ifPresent(loanRequest::setBranchManager);
        // }

        // Set Back Office (optional)
        // if (payload.containsKey("backOfficeId")) {
        // userRepository.findById(UUID.fromString(payload.get("backOfficeId").toString()))
        // .ifPresent(loanRequest::setBackOffice);
        // }

        LoanRequest savedLoanRequest = loanRequestRepository.save(loanRequest);
        return LoanRequestDto.fromEntity(savedLoanRequest);
    }

    public List<LoanRequestDto> getAllLoanRequests() {
        return loanRequestRepository.findAll()
                .stream()
                .map(LoanRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    public LoanRequest getLoanRequestById(String id) {
        return loanRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
    }

    public LoanRequestDto marketingAction(String id, Map<String, Object> payload, String token) {
        LoanRequest loanRequest = loanRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        if (payload.containsKey("marketing_approval")) {
            String emailFromToken = jwtUtil.extractEmail(token);

            User marketing = loanRequest.getMarketing();
            if (marketing != null && emailFromToken.equalsIgnoreCase(marketing.getEmail())) {
                Boolean approval = Boolean.parseBoolean(payload.get("marketing_approval").toString());
                if (approval) {
                    loanRequest.setBranchManager(loanRequest.getBranch().getBranchManager());
                }
                loanRequest.setMarketingApprove(approval);
            } else {
                throw new AccessDeniedException("You are not authorized to approve or reject as Marketing.");
            }
        }
        LoanRequest savedLoanRequest = loanRequestRepository.save(loanRequest);
        return LoanRequestDto.fromEntity(savedLoanRequest);
    }

    public LoanRequestDto branchManagerAction(String id, Map<String, Object> payload, String token) {
        LoanRequest loanRequest = loanRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        if (loanRequest.getMarketingApprove() == null) {
            throw new IllegalArgumentException("Contact your Marketing to Approve first");
        } else if (!loanRequest.getMarketingApprove()) {
            throw new IllegalArgumentException("Already Rejected by Marketing");
        }

        if (payload.containsKey("branch_manager_approval")) {
            String emailFromToken = jwtUtil.extractEmail(token);

            User marketing = loanRequest.getBranchManager();
            if (marketing != null && emailFromToken.equalsIgnoreCase(marketing.getEmail())) {
                loanRequest.setBranchManagerApprove(
                        Boolean.parseBoolean(payload.get("branch_manager_approval").toString()));
            } else {
                throw new AccessDeniedException("You are not authorized to approve or reject as Branch Manager.");
            }
        }
        LoanRequest savedLoanRequest = loanRequestRepository.save(loanRequest);
        return LoanRequestDto.fromEntity(savedLoanRequest);
    }

    @Transactional
    public LoanRequestDto backOfficeProceed(String id, String token) {
        LoanRequest loanRequest = loanRequestRepository.findWithLockById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        if (loanRequest.getMarketingApprove() == null) {
            throw new IllegalArgumentException("Contact your Marketing to Approve first");
        } else if (!loanRequest.getMarketingApprove()) {
            throw new IllegalArgumentException("Already Rejected by Marketing");
        }
        if (loanRequest.getBranchManagerApprove() == null) {
            throw new IllegalArgumentException("Contact your Branch Manager to Approve first");
        } else if (!loanRequest.getBranchManagerApprove()) {
            throw new IllegalArgumentException("Already Rejected by Branch Manager");
        }
        if (loanRequest.getBackOffice() == null) {
            String email = jwtUtil.extractEmail(token);
            User backOffice = userService.getUserByEmail(email);
            loanRequest.setBackOffice(backOffice);
            LoanRequest savedLoanRequest = loanRequestRepository.save(loanRequest);
            return LoanRequestDto.fromEntity(savedLoanRequest);
        } else {
            throw new IllegalArgumentException("Request already taken by other Officer");
        }

    }

    public LoanRequestDto backOfficeDisbursement(String id, Map<String, Object> payload, String token) {
        LoanRequest loanRequest = loanRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        if (payload.containsKey("back_office_approval_disbursement")) {
            String emailFromToken = jwtUtil.extractEmail(token);

            User backOffice = loanRequest.getBackOffice();
            if (backOffice != null && emailFromToken.equalsIgnoreCase(backOffice.getEmail())) {
                Boolean approval = Boolean.parseBoolean(payload.get("back_office_approval_disbursement").toString());
                loanRequest.setBackOfficeApproveDisburse(approval);
            } else {
                throw new AccessDeniedException("You are not authorized to disburse loan as Back Office.");
            }
        }
        LoanRequest savedLoanRequest = loanRequestRepository.save(loanRequest);
        return LoanRequestDto.fromEntity(savedLoanRequest);
    }

    public LoanRequest updateLoanRequest(String id, Map<String, Object> payload, String token) {
        LoanRequest loanRequest = loanRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        if (payload.containsKey("amount")) {
            loanRequest.setAmount(payload.get("amount").toString());
        }

        if (payload.containsKey("marketing_approval")) {
            loanRequest.setMarketingApprove(Boolean.parseBoolean(payload.get("marketing_approval").toString()));
        }

        if (payload.containsKey("branchManagerApprove")) {
            loanRequest.setBranchManagerApprove(Boolean.parseBoolean(payload.get("branchManagerApprove").toString()));
        }

        if (payload.containsKey("backOfficeApprove")) {
            loanRequest.setBackOfficeApproveDisburse(Boolean.parseBoolean(payload.get("backOfficeApprove").toString()));
        }

        // return loanRequestRepository.save(loanRequest);
        return loanRequest;
    }

    public void deleteLoanRequest(String id) {
        LoanRequest loanRequest = loanRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        loanRequestRepository.delete(loanRequest);
    }
}
