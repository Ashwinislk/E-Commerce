package com.electronicstore.service;

import com.electronicstore.entity.User;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.UserDto;
import com.electronicstore.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    User user;

    User user1;

    @BeforeEach
    public void init() {
        user = User.builder()
                .name("Ashwini")
                .email("ashwinislk@gmail.com")
                .about("I am java developer")
                .gender("Female")
                .imageName("default.png")
                .password("ashwini123")
                .build();
        user1 = User.builder()
                .name("Ashwini")
                .email("ashwinislk@gmail.com")
                .about("I am PHP developer")
                .gender("Female")
                .imageName("default.png")
                .password("ashwinislk123")
                .build();
    }
    @Test
    public void createUserTest(){
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto userDto = userService.createUser(modelMapper.map(user, UserDto.class));

        Assertions.assertEquals("Ashwini",userDto.getName());
        Assertions.assertNotNull(userDto);
    }
    @Test
    public void updateUserTest(){
       UserDto userDto = UserDto.builder()
                .name("Komal")
                .email("komal@gmail.com")
                .about("I am PHP developer")
                .gender("Female")
                .imageName("default.png")
                .password("komal123")
                .build();

       Mockito.when(userRepository.findById("userId")).thenReturn(Optional.of(user));
       Mockito.when(userRepository.save(user)).thenReturn(user);
        UserDto userDto1 = userService.updateUser(userDto, "userId");
        Assertions.assertNotNull(userDto1);
        Assertions.assertEquals("Komal",userDto1.getName());

    }
    @Test
    public void getSingleIdTest(){
        String userId="abc";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDto userDto = userService.getSingleUser(userId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(),userDto.getName());
        Assertions.assertEquals(user.getUserId(),userDto.getUserId());
    }
    @Test
    public void getByEmailIdTest(){

        String emailId="ashslk@gmail.com";
        Mockito.when(userRepository.findByEmail(emailId)).thenReturn(Optional.of(user));
        UserDto userByEmailId = userService.getUserByEmailId(emailId);
        Assertions.assertNotNull(userByEmailId);
        Assertions.assertEquals(user.getEmail(),userByEmailId.getEmail());//expected actual

    }
    @Test
     public void getByEmailAndPassTest(){
        String email="koaml@gmail.com";
        String password="komal123";
        Mockito.when(userRepository.findByEmailAndPassword(email,password)).thenReturn(Optional.of(user));
        UserDto userDto = userService.getUserByEmailAndPassword(email, password);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getEmail(),userDto.getEmail());
        Assertions.assertEquals(user.getPassword(),userDto.getPassword());
    }
    @Test
    public void getAllUserTest(){
        List<User> users = Arrays.asList(user, user1);
        Page<User> page=new PageImpl<>(users);
        Mockito.when(userRepository.findAll((Pageable)Mockito.any())).thenReturn(page);

        Sort sort =Sort.by("name").ascending();
    }
   @Test
    public void searchUserTest(){
        String keyword="Ashwini";
        Mockito.when(userRepository.findByNameContaining(keyword)).thenReturn(List.of(user,user1));
       List<UserDto> userDtos = userService.searchUser(keyword);
       Assertions.assertEquals(2,userDtos.size());

    }
    @Test
    public void deleteUserByIdTest(){
        String userId="abc";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }

}
