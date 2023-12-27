package com.electronicstore.config;

import com.electronicstore.security.JwtAuthenticationEntryPoint;
import com.electronicstore.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        // create Users
////     In Memory Security Using SpringBoot
//        UserDetails admin = User.builder()
//                .username("komal")
//                .password(passwordEncoder().encode("komal"))
//                .roles("ADMIN")
//
//                .build();
//
//        UserDetails normal = User.builder()
//                .username("ashwini")
//                .password(passwordEncoder().encode("ashwini"))
//                .roles("NORMAL")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, normal);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest().authenticated().and()
//                .formLogin().loginPage("login.html")
//                .loginProcessingUrl("/process_url")
//                .defaultSuccessUrl("/dashboard")
//                .failureUrl("error")
//                .and()
//                .logout()
//                .logoutUrl("/logout");

     http.csrf().disable()
             .cors().disable()
             .authorizeRequests()
             .antMatchers("/api/auth/login")
             .permitAll()
             .antMatchers(HttpMethod.POST,"/api/users")
             .permitAll()
             .antMatchers(HttpMethod.DELETE,"/api/users/delete/")
             .hasRole("ADMIN")
             .anyRequest()
             .authenticated()
             .and()
             .exceptionHandling()
             .authenticationEntryPoint(authenticationEntryPoint)
             .and()
             .sessionManagement()
             .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
     http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

       return http.build();
   }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
       return builder.getAuthenticationManager();
    }
}

