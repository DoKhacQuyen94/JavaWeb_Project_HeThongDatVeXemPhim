package org.example.hethongquanlyvexemphim.service;

import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.model.UserProfile;

import java.util.Optional;

public interface IUserProfileService {
    UserProfile getUserProfile(Integer userId);
    void updateUserProfile(User user, String fullName, String phone, String oldPassword, String newPassword, String confirmPassword);
}
