package com.electronicstore.payload;

import com.electronicstore.validation.ImageNameValid;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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


    private String email;

    @NotBlank(message = "Please enter valid password")
    private String password;

    @ImageNameValid(message = "Image Name must not be Blank")
    private String imageName;

    @NotBlank
    private String gender;

    @NotBlank
    @Size(message = "Please Enter About User")
    private String about;

}

