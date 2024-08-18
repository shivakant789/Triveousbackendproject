package com.shiva.ecommerceapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//exclude = {SecurityAutoConfiguration.class }

@SpringBootApplication()
public class EcommerceapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceapiApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @EnableWebSecurity
//    public class WebSecurityConfig {
//
//        @Bean
//        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//            // ...
//           // http.cors(Customizer.withDefaults()); // disable this line to reproduce the CORS 401
//            return http.build();
//        }
//    }

//    @EnableWebSecurity
//    public class SecurityConfig {
//
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                    .csrf().disable()
//                    .authorizeRequests()
//                    .antMatchers("/api/public/**").permitAll() // Allow public access to this endpoint
//                  //  .antMatchers("/api/admin/**").hasRole("ADMIN") // Protect admin endpoints
//                    .anyRequest().authenticated()
//                    .and()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//            // Add your JWT filter here if necessary
//
//            return http.build();
        }



