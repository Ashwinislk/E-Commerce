package com.electronicstore;

import com.electronicstore.entity.Role;
import com.electronicstore.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
   @Value("${admin.role.id}")
	private String role_admin_id;

	@Value("${normal.role.id}")
	private String role_normal_id;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("komal123"));

		try{
			Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
			Role role_normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
			roleRepository.save(role_admin);
			roleRepository.save(role_normal);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
