package com.electronicstore.serviceImpl;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.entity.User;
import com.electronicstore.exception.ResourceNotFound;
import com.electronicstore.payload.Helper;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.UserDto;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Entering the dao call for save userdata");
        String str = UUID.randomUUID().toString();
        userDto.setUserId(str);
        User user = this.modelMapper.map(userDto, User.class);
        this.userRepository.save(user);
        UserDto userDto1 = this.modelMapper.map(user, UserDto.class);
        log.info("Completed the dao call for save userdata");
        return userDto1;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String id) {
        log.info("Entering the dao call for update userdata with id :{}",id);
        User user = this.userRepository.findById(id).orElseThrow(()->new ResourceNotFound(AppConstant.NOT_FOUND + id));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        this.userRepository.save(user);
        UserDto userDto1 = this.modelMapper.map(user, UserDto.class);
        log.info("Completed the dao call for update userdata with id :{}",id);
        return userDto1;
    }

    @Override
    public UserDto getSingleUser(String id) {
        log.info("Entering the dao call for get single userdata :{}",id);
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + id));
        UserDto dto = this.modelMapper.map(user, UserDto.class);
        log.info("Completed the dao call for get single userdata :{}",id);
        return dto;
    }

    @Override
    public void deleteUser(String id) {
        log.info("Entering the dao call for delete single userdata :{}",id);
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + id));
        log.info("Completed the dao call for delete single userdata :{}",id);
        this.userRepository.delete(user);

    }

    @Override
    public PageableResponse<UserDto> getAllUsers(Integer pageNumber,Integer pageSize,String sortBy,String sortDirection) {
        log.info("Entering the dao call for get all userdata");
        Sort desc = (sortDirection.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        PageRequest pr = PageRequest.of(pageNumber, pageSize,desc);
        Page<User> pages = this.userRepository.findAll(pr);
        PageableResponse<UserDto> response = Helper.getPageableResponse(pages, UserDto.class);
        log.info("Completed the dao call for get all userdata");
        return response;
    }

    @Override
    public UserDto getUserByEmailId(String id) {
        log.info("Entering the dao call for get user email id:{}",id);
        User user = this.userRepository.findByEmail(id).orElseThrow(() -> new ResourceNotFound( AppConstant.NOT_FOUND + id));
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        log.info("Completed the dao call for get user email id:{}",id);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        log.info("Entering the dao call for get user email id:{}",keyword);
        List<User> users = this.userRepository.findByNameContaining(keyword);
        List<UserDto> dto = users.stream().map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        log.info("Completed the dao call for get user email id:{}",keyword);
        return dto;
    }

    @Override
    public UserDto getUserByEmailAndPassword(String email, String password) {
        log.info("Entering the dao call for get user email and password:{} and :{}",email,password);
        User user = this.userRepository.findByEmailAndPassword(email, password).get();
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        log.info("Completed the dao call for get user email and password:{} and :{}",email,password);
        return userDto;

    }
}