package com.unievent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import java.util.Optional;

@SpringBootApplication
@EnableAsync
public class UniEventApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniEventApplication.class, args);
	}

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner initData(
            com.unievent.repository.UserRepository userRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                String adminEmail = "superadmin@gmail.com".toLowerCase().trim();
                System.out.println(">>> SEED: Initializing admin check for: " + adminEmail);
                
                Optional<com.unievent.entity.User> existingAdmin = userRepository.findByEmail(adminEmail);
                com.unievent.entity.User admin;

                if (existingAdmin.isPresent()) {
                    System.out.println(">>> SEED: Found existing record, updating for fresh state...");
                    admin = existingAdmin.get();
                } else {
                    System.out.println(">>> SEED: Creating new super admin...");
                    admin = new com.unievent.entity.User();
                    admin.setEmail(adminEmail);
                }

                admin.setName("Super Admin");
                admin.setPassword(passwordEncoder.encode("Admin2004@"));
                admin.setRole(com.unievent.entity.Role.ADMIN);
                admin.setApprovalStatus(com.unievent.entity.User.ApprovalStatus.APPROVED);
                admin.setActive(true);
                admin.setUniversityId("ADMIN01");
                admin.setVerified(true);
                
                userRepository.save(admin);
                System.out.println(">>> SEED SUCCESS: Super Admin fully stored: " + adminEmail + " / Admin2004@");
                
            } catch (Exception e) {
                System.err.println(">>> SEED ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

}
