package com.eca.backend.userservice.config;

import com.eca.backend.userservice.model.UserEntity;
import com.eca.backend.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                UserEntity u1 = new UserEntity();
                u1.setName("Ruwani Ranthika");
                u1.setEmail("ruwani@swiftcart.com");
                u1.setAvatarUrl("https://storage.googleapis.com/swiftcart-avatars-bucket-ruwani/default-avatar.png");

                UserEntity u2 = new UserEntity();
                u2.setName("Savinda Jay");
                u2.setEmail("savinda@swiftcart.com");
                u2.setAvatarUrl("https://storage.googleapis.com/swiftcart-avatars-bucket-ruwani/savinda.png");

                repository.saveAll(List.of(u1, u2));
            }
        };
    }
}
