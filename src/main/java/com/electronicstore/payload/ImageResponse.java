package com.electronicstore.payload;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {

    private  String imageName;

    private String message;

    private Boolean status;

    private HttpStatus httpStatus;
}
