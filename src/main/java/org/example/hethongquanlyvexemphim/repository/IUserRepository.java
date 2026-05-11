package org.example.hethongquanlyvexemphim.repository;

import org.example.hethongquanlyvexemphim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email); // Dùng cho đăng nhập và kiểm tra trùng email
}