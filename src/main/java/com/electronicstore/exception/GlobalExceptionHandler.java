package com.electronicstore.exception;

import com.electronicstore.payload.ApiResponse;
import com.electronicstore.payload.ImageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiResponse> resouceNotFoundExceptionHandler(ResourceNotFound ex) {

        ApiResponse api = ApiResponse.builder().message(ex.getMessage()).status(false).httpcode((HttpStatus.NOT_FOUND)).build();
        return new ResponseEntity<ApiResponse>(api, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> mathodArgumentNotValidHanlder(MethodArgumentNotValidException ex) {

        Map<String,String>map=new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(e-> map.put(e.getField(),e.getDefaultMessage()));

        return new ResponseEntity<Map<String,String>>(map,HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponse> badApiRequest(BadApiRequest ex) {

        ApiResponse api = ApiResponse.builder().message(ex.getMessage()).status(false).httpcode((HttpStatus.BAD_REQUEST)).build();
        return new ResponseEntity<ApiResponse>(api, HttpStatus.BAD_REQUEST);

    }

}
