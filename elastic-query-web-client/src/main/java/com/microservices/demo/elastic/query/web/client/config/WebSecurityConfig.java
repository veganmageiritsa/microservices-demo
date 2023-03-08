package com.microservices.demo.elastic.query.web.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.microservices.demo.config.UserConfigData;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserConfigData userConfigData;

    public WebSecurityConfig(final PasswordEncoder passwordEncoder,
                             UserConfigData userData) {
        this.passwordEncoder = passwordEncoder;
        this.userConfigData = userData;
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/**").hasRole("USER")
//                .anyRequest()
//                .fullyAuthenticated();
//    }

  
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser(userConfigData.getUsername())
            .password(passwordEncoder.encode(userConfigData.getPassword()))
            .authorities(userConfigData.getRoles());
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                        .httpBasic()
                        .and()
                        .authorizeRequests()
                        .antMatchers("/").permitAll()
                        .antMatchers("/**").hasRole("USER")
                                                       .anyRequest()
                        .fullyAuthenticated()
                ;
        
        return http.build();
    }
    
}
