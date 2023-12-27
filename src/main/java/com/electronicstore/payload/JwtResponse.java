package com.electronicstore.payload;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private  String jwtToken;

    private  UserDto user;
}
