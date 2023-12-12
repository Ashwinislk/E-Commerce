package com.electronicstore.controller;

import com.electronicstore.entity.User;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.UserDto;
import com.electronicstore.service.FileService;
import com.electronicstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    FileService fileService;

    User user;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        user = User.builder()
                .name("Ashwini")
                .email("ashwinislk@gmail.com")
                .about("I am java developer")
                .gender("Female")
                .imageName("default.png")
                .password("ashwini123")
                .build();
    }
    @Test
    public void createUserTest() throws Exception {
        UserDto dto=modelMapper.map(user,UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());

    }
    private String convertObjectToJsonString(User user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    @Test
    public void getSingleUserTest() throws Exception {
        String userId="123";
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.getSingleUser(userId)).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/userid/"+userId))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void updateUserTest() throws Exception {
        String userId="abc";
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" +userId).contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(user)).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").exists());

    }
    @Test
    public void getAllUsersTest() throws Exception {
        UserDto userDto = UserDto.builder().name("ashwini")
                .email("ash@123")
                .about("java developer")
                .gender("female")
                .password("ash123")
                .imageName("default.png").build();

        UserDto userDto1 = UserDto.builder().name("komal")
                .email("komal@123")
                .about("php developer")
                .gender("female")
                .password("komal123")
                .imageName("default.png").build();
        PageableResponse<UserDto> pages=new PageableResponse<>();

        pages.setContent(Arrays.asList(userDto,userDto1));
        pages.setLastPage(false);
        pages.setPageNumber(100);
        pages.setPageSize(10);
        pages.setTotalElement(1000);
        Mockito.when(userService.getAllUsers(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pages);
       this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/allusers").contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON)).andDo(print())
               .andExpect(status().isOk());

    }
    @Test
    public void deleteUserTest() throws Exception {
        String userId="123";
       Mockito.doNothing().when(userService).deleteUser(userId);
       mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete/"+userId)).andDo(print())
               .andExpect(status().isOk());
    }
    @Test
    public  void getByEmailTest() throws Exception {
        String email="ash@123";
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.getUserByEmailId(Mockito.anyString())).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/email/"+email)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUserByEmailAndPassTest() throws Exception {
        String email="ash@123";
        String pass="ash123";
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.getUserByEmailAndPassword(Mockito.anyString(),Mockito.anyString())).thenReturn(userDto);
      mockMvc.perform(MockMvcRequestBuilders.get("/api/users/email/"+email+"/pass/"+pass)).andDo(print())
              .andExpect(status().isOk());
    }
    @Test
    public void getUserNameContainingTest() throws Exception {
        UserDto userDto = UserDto.builder().name("ashwini")
                .email("ash@123")
                .about("java developer")
                .gender("female")
                .password("ash123")
                .imageName("default.png").build();

        UserDto userDto1 = UserDto.builder().name("komal")
                .email("komal@123")
                .about("php developer")
                .gender("female")
                .password("komal123")
                .imageName("default.png").build();
        String keyword="ashwini";
        Mockito.when(userService.searchUser(Mockito.anyString())).thenReturn(Arrays.asList(userDto,userDto1));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search/"+keyword))
                .andDo(print()).andExpect(status().isOk());
    }
}
