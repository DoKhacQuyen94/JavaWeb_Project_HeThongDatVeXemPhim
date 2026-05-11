package org.example.hethongquanlyvexemphim.repository;

import org.example.hethongquanlyvexemphim.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserProfileRepository extends JpaRepository<UserProfile, Integer> {

}