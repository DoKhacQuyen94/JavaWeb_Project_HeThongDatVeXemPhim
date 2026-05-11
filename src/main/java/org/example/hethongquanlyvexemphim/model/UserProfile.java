package org.example.hethongquanlyvexemphim.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserProfile {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}