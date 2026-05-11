package org.example.hethongquanlyvexemphim.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.model.UserProfile;
import org.example.hethongquanlyvexemphim.repository.IUserProfileRepository;
import org.example.hethongquanlyvexemphim.repository.IUserRepository;
import org.example.hethongquanlyvexemphim.service.HashPass;
import org.example.hethongquanlyvexemphim.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IUserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public void registerUser(User user) {
        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        user.setPassword(HashPass.hash(user.getPassword()));
        user.setRole("user");
        user.setStatus("active");
        user.setCreatedAt(LocalDateTime.now());

        // Lưu user và nhận lại đối tượng đã có ID
        User savedUser = userRepository.save(user);

        // Khởi tạo Profile đi kèm
        UserProfile profile = new UserProfile();
        profile.setUser(savedUser); // Sử dụng savedUser để khớp @MapsId
        profile.setFullName("Người dùng mới");
        profile.setAvatar("default_avatar.png");

        userProfileRepository.save(profile);
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && HashPass.check(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}