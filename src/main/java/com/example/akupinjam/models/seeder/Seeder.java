package com.example.akupinjam.models.seeder;

import com.example.akupinjam.models.Role;
import com.example.akupinjam.repositories.RoleRepository;
import com.example.akupinjam.models.User;
import com.example.akupinjam.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @jakarta.transaction.Transactional
    public void run(String... args) throws Exception {
        // Cek apakah role sudah ada
        if (roleRepository.count() == 0) {
            Role superadmin = new Role();
            superadmin.setName("superadmin");
            roleRepository.save(superadmin);

            Role customer = new Role();
            customer.setName("customer");
            roleRepository.save(customer);

            Role marketing = new Role();
            marketing.setName("marketing");
            roleRepository.save(marketing);

            Role branchManager = new Role();
            branchManager.setName("branch manager");
            roleRepository.save(branchManager);

            Role backOffice = new Role();
            backOffice.setName("back office");
            roleRepository.save(backOffice);
        }

        // Buat superadmin jika belum ada
        if (userRepository.count() == 0) {
            User superAdmin = new User();
            superAdmin.setName("Superadmin");
            superAdmin.setActive(true);
            superAdmin.setEmail("superadmin@gmail.com");
            superAdmin.setPassword(passwordEncoder.encode("superadmin123"));
            superAdmin.setRole(roleRepository.findByName("superadmin").orElse(null));
            userRepository.save(superAdmin);
            
            User marketing = new User();
            marketing.setName("Marketing");
            marketing.setActive(true);
            marketing.setEmail("marketing@gmail.com");
            marketing.setPassword(passwordEncoder.encode("marketing123"));
            marketing.setRole(roleRepository.findByName("marketing").orElse(null));
            userRepository.save(marketing);

            User customer = new User();
            customer.setName("Customer");
            customer.setActive(true);
            customer.setEmail("customer@gmail.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setRole(roleRepository.findByName("customer").orElse(null));
            userRepository.save(customer);

            User branchManager = new User();
            branchManager.setName("Branch Manager");
            branchManager.setActive(true);
            branchManager.setEmail("branchmanager@gmail.com");
            branchManager.setPassword(passwordEncoder.encode("branchmanager123"));
            branchManager.setRole(roleRepository.findByName("branch manager").orElse(null));
            userRepository.save(branchManager);
            
            User backOffice = new User();
            backOffice.setName("Back Office");
            backOffice.setActive(true);
            backOffice.setEmail("backoffice@gmail.com");
            backOffice.setPassword(passwordEncoder.encode("backoffice123"));
            backOffice.setRole(roleRepository.findByName("back office").orElse(null));
            userRepository.save(backOffice);
        }
    }
}
