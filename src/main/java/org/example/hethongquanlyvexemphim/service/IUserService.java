package org.example.hethongquanlyvexemphim.service;


import org.example.hethongquanlyvexemphim.model.User;

public interface IUserService {
    void registerUser(User user);
    User login(String email, String password);
}
