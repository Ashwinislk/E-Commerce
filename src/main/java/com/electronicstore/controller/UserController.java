package com.electronicstore.controller;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.payload.ApiResponse;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.UserDto;
import com.electronicstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user){
        log.info("Entering the request for save user data");
        UserDto user1 = this.userService.createUser(user);
        log.info("Completed the request for save user data");
        return new ResponseEntity<UserDto>(user1 , HttpStatus.CREATED);
    }
    @PutMapping("/update/users/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user,@PathVariable String id){
        log.info("Entering the request for save user data :{}",id);
        UserDto userDto = this.userService.updateUser(user,id);
        log.info("Completed the request for save user data :{}",id);
        return new ResponseEntity<UserDto>(userDto , HttpStatus.CREATED);
    }
    @GetMapping("users/userid/{id}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable String id){
        log.info("Entering the request for get single user data :{}",id);
        UserDto user = this.userService.getSingleUser(id);
        log.info("Completed the request for get single user data :{}",id);
        return new ResponseEntity<UserDto>(user,HttpStatus.OK);
    }
    @DeleteMapping("users/delete/{userid}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userid){
        log.info("Entering the request for delete user data :{}",userid);
        ApiResponse api=new ApiResponse();
        api.setMessage(AppConstant.DELETE);
        api.setHttpcode(HttpStatus.OK);
        api.setStatus(true);
        this.userService.deleteUser(userid);
        log.info("Completed the request for delete user data :{}",userid);
        return new ResponseEntity<ApiResponse>(api,HttpStatus.OK);
    }
    @GetMapping("users/allusers")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
        @RequestParam (value = "pageNumber",defaultValue = AppConstant.PAGE_NUMBER,required = false)Integer pageNumber,
        @RequestParam(value = "pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize ,
         @RequestParam(value = "sortBy",defaultValue = AppConstant.SORT_BY,required = false) String sortBy,
        @RequestParam(value = "sortDirection",defaultValue = AppConstant.SORT_DIR,required = false) String sortDirection)
    {
        log.info("Entering the request for get all user data");
        PageableResponse<UserDto> allUsers = this.userService.getAllUsers(pageNumber, pageSize, sortBy, sortDirection);
        log.info("Completed the request for get all user data");
        return new ResponseEntity<PageableResponse<UserDto>>(allUsers, HttpStatus.OK);
    }
    @GetMapping("users/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        log.info("Entering the request for get user email data :{}",email);
        UserDto userByEmailId = this.userService.getUserByEmailId(email);
        log.info("Completed the request for get user email data :{}",email);
        return new ResponseEntity<UserDto>(userByEmailId, HttpStatus.OK);
    }

    @GetMapping("users/email/{email}/pass/{password}")
    public ResponseEntity<UserDto> getUserByEmailAndPass(@PathVariable String email, @PathVariable String password) {
        log.info("Entering the request for get user email and user password :{} and :{}",email,password);
        UserDto userByEmailAndPassword = this.userService.getUserByEmailAndPassword(email, password);
        log.info("Completed the request for get user email and user password :{} and :{}",email,password);
        return  new ResponseEntity<UserDto>(userByEmailAndPassword,HttpStatus.OK);
    }

    @GetMapping("users/search/{keyword}")
    public ResponseEntity<List<UserDto>> getUserNameContaing(@PathVariable String keyword) {
        log.info("Entering the request for get user name containing :{}",keyword);
        List<UserDto> userDtos = this.userService.searchUser(keyword);
        log.info("Completed the request for get user name containing :{}",keyword);
        return new ResponseEntity<List<UserDto>>(userDtos, HttpStatus.OK);
    }



}
