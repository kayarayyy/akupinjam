package com.example.akupinjam.models.seeder;

import com.example.akupinjam.models.Feature;
import com.example.akupinjam.models.Role;
import com.example.akupinjam.models.RoleFeature;
import com.example.akupinjam.models.User;
import com.example.akupinjam.repositories.FeatureRepository;
import com.example.akupinjam.repositories.RoleFeatureRepository;
import com.example.akupinjam.repositories.RoleRepository;
import com.example.akupinjam.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.List;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private RoleFeatureRepository roleFeatureRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedFeatures();
        seedRoleFeatures();
        seedUsers();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "SUPERADMIN", null));
            roleRepository.save(new Role(null, "CUSTOMER", null));
            roleRepository.save(new Role(null, "MARKETING", null));
            roleRepository.save(new Role(null, "BRANCH_MANAGER", null));
            roleRepository.save(new Role(null, "BACK_OFFICE", null));
        }
    }

    private void seedFeatures() {
        if (featureRepository.count() == 0) {
            featureRepository.save(new Feature(null, "MANAGE_USERS", null));
            featureRepository.save(new Feature(null, "MANAGE_ROLES", null));
            featureRepository.save(new Feature(null, "MANAGE_ROLE_FEATURES", null));
            featureRepository.save(new Feature(null, "MANAGE_FEATURES", null));
            featureRepository.save(new Feature(null, "MANAGE_PROFILE", null));
        }
    }

    private void seedRoleFeatures() {
        if (roleFeatureRepository.count() == 0) {

            Role superAdmin = roleRepository.findByName("SUPERADMIN").orElse(null);
            Feature manageUsers = featureRepository.findByName("MANAGE_USERS").orElse(null);
            Feature manageRoles = featureRepository.findByName("MANAGE_ROLES").orElse(null);
            Feature manageFeatures = featureRepository.findByName("MANAGE_FEATURES").orElse(null);
            Feature manageRoleFeatures = featureRepository.findByName("MANAGE_ROLE_FEATURES").orElse(null);
            
            Role customer = roleRepository.findByName("CUSTOMER").orElse(null);
            Feature manageProfile = featureRepository.findByName("MANAGE_PROFILE").orElse(null);
            
            if (superAdmin != null) {
                if (manageRoles != null) {
                    roleFeatureRepository.save(new RoleFeature(null, superAdmin, manageRoles));
                }
                if (manageUsers != null) {
                    roleFeatureRepository.save(new RoleFeature(null, superAdmin, manageUsers));
                }
                if (manageFeatures != null) {
                    roleFeatureRepository.save(new RoleFeature(null, superAdmin, manageFeatures));
                }
                if (manageRoleFeatures != null) {
                    roleFeatureRepository.save(new RoleFeature(null, superAdmin, manageRoleFeatures));
                }
            }
            if (customer != null) {
                if (manageProfile != null) {
                    roleFeatureRepository.save(new RoleFeature(null, customer, manageProfile));
                }
            }
        }
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            createUser("Superadmin", "superadmin@gmail.com", "superadmin123", "SUPERADMIN");
            createUser("Marketing", "marketing@gmail.com", "marketing123", "MARKETING");
            createUser("Customer", "customer@gmail.com", "customer123", "CUSTOMER");
            createUser("Branch Manager", "branchmanager@gmail.com", "branchmanager123", "BRANCH_MANAGER");
            createUser("Back Office", "backoffice@gmail.com", "backoffice123", "BACK_OFFICE");
        }
    }

    private void createUser(String name, String email, String password, String roleName) {
        User user = new User();
        user.setName(name);
        user.setActive(true);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(roleRepository.findByName(roleName).orElse(null));
        userRepository.save(user);
    }
}
