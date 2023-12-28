package com.electronicstore.service;

import com.electronicstore.entity.User;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public UserDto createUser(UserDto userDto);

    public UserDto updateUser(UserDto userDto, String id);

    public UserDto getSingleUser(String id);

    void deleteUser(String id);

    public PageableResponse<UserDto> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);

    public UserDto getUserByEmailId(String id);

    public List<UserDto> searchUser(String keyword);

    public UserDto getUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByEmailOptional(String email);

}
