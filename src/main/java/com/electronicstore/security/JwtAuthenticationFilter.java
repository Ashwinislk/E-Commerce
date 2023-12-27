package com.electronicstore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private  JwtHelper jwtHelper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        log.info("Header : {}",requestHeader);

        String username=null;
        String token=null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer")) {
            token = requestHeader.substring(7);
            try {
                username = this.jwtHelper.getUsernamefromToken(token);

            } catch (IllegalArgumentException ex) {
                log.info("IllegalArgument Exception while fetching UserName");
                ex.printStackTrace();

            } catch (ExpiredJwtException ex) {
                log.info("Given Jwt Token is Expired");
                ex.printStackTrace();

            } catch (MalformedJwtException ex) {
                log.info("Something Went Wrong With Jwt Token ");
                ex.printStackTrace();
            }
        }else {
            log.info("Invalid  Header Value");

        }
                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                    Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
                    if (validateToken) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        log.info("Validation fails");
                    }
                }


               filterChain.doFilter(request,response);
            }
        }


