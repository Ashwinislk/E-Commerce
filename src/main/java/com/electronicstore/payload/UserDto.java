package com.electronicstore.payload;

import com.electronicstore.validation.ImageNameValid;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @NotBlank
    @Size(min = 5,max = 20,message = "UserName Should be Min 5 Character And Max 30 Character")
    private String name;

    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid User Email!")
    private String email;

//    @Pattern( regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&*!])[A-Za-z\\d@#$%^&*!]{8,}$",
//    message = "password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character")
    private String password;

    @ImageNameValid(message = "Image Name must not be Blank")
    private String imageName;

    @Size(min=4,max=6,message = "Invalid Gender")
    private String gender;

    @NotBlank(message = "Write Something About Yourself")
    private String about;

    private Set<RoleDto> roles=new HashSet<>();
}

