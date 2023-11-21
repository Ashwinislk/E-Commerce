package com.electronicstore.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String userId;

    @Column(name="user_name")
    private String name;

    @Column(name="user_email")
    private String email;

    @Column(name="user_password")
    private String password;

    @Column(name="user_gender")
    private String gender;

    @Column(name="user_about")
    private String about;

    @Column(name = "user_image_name")
    private String imageName;

}
