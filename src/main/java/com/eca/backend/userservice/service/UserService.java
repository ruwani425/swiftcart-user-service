package com.eca.backend.userservice.service;

import com.eca.backend.userservice.model.UserEntity;
import com.eca.backend.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GcsStorageService gcsStorageService;

    public UserService(UserRepository userRepository, GcsStorageService gcsStorageService) {
        this.userRepository = userRepository;
        this.gcsStorageService = gcsStorageService;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity updateUserAvatar(Long id, MultipartFile file) throws IOException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        String avatarUrl = gcsStorageService.uploadUserAvatar(id, file);
        user.setAvatarUrl(avatarUrl);

        return userRepository.save(user);
    }
}
