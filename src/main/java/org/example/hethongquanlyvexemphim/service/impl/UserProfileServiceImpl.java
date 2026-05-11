package org.example.hethongquanlyvexemphim.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.model.UserProfile;
import org.example.hethongquanlyvexemphim.repository.IUserProfileRepository;
import org.example.hethongquanlyvexemphim.repository.IUserRepository;
import org.example.hethongquanlyvexemphim.service.HashPass;
import org.example.hethongquanlyvexemphim.service.IUserProfileService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements IUserProfileService {
    private final IUserProfileRepository userProfileRepository;
    private final IUserRepository userRepository;
    @Override
    public UserProfile getUserProfile(Integer userId) {
        return userProfileRepository.findById(userId).orElse(null);
    }
    @Override
    @Transactional // Quan trọng: Đảm bảo nếu lỗi 1 bảng thì bảng kia sẽ tự rollback
    public void updateUserProfile(User user, String fullName, String phone,
                                  String oldPassword, String newPassword, String confirmPassword) {

        // --- BƯỚC 1: CẬP NHẬT THÔNG TIN PROFILE ---
        // Tìm profile theo ID người dùng
        UserProfile profile = userProfileRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ người dùng!"));

        profile.setFullName(fullName);
        profile.setPhone(phone);
        userProfileRepository.save(profile);

        // --- BƯỚC 2: XỬ LÝ ĐỔI MẬT KHẨU (NẾU CÓ NHẬP) ---
        if (newPassword != null && !newPassword.isEmpty()) {

            // 1. Kiểm tra mật khẩu cũ
            if (oldPassword == null || oldPassword.isEmpty()) {
                throw new RuntimeException("Bạn phải nhập mật khẩu cũ để xác nhận!");
            }

            // 2. So khớp mật khẩu cũ với mật khẩu đã mã hóa trong DB
            if (!HashPass.check(oldPassword, user.getPassword())) {
                throw new RuntimeException("Mật khẩu hiện tại không chính xác!");
            }

            // 3. Kiểm tra mật khẩu mới và xác nhận
            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("Xác nhận mật khẩu mới không khớp!");
            }

            if (newPassword.length() < 6) {
                throw new RuntimeException("Mật khẩu mới phải từ 8 ký tự trở lên!");
            }

            // 4. Mã hóa mật khẩu mới và cập nhật vào bảng User
            user.setPassword(HashPass.hash(newPassword));
            userRepository.save(user); // Lưu vào bảng User
        }
    }
}
