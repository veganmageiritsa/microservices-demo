package com.microservices.demo.elastic.query.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.microservices.demo.config.UserConfigData;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    
   
    private final PasswordEncoder passwordEncoder;
    private final UserConfigData userConfigData;
    private static final String[] NO_AUTH_ANT_PATTERNS =
        new String[]{ "/**" };
    
    public WebSecurityConfig(final PasswordEncoder passwordEncoder,
                             final UserConfigData userConfigData) {
        this.passwordEncoder = passwordEncoder;
        this.userConfigData = userConfigData;
    }
    
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
//        Security.addProvider(new BouncyCastleProvider());
        http.authorizeRequests()
            .antMatchers("/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and()
            .csrf().disable();
//        http
//            .httpBasic()
//            .and()
//            .authorizeRequests()
//            .antMatchers("/**").hasAnyRole("USER")
//            .and()
//            .csrf().disable();
        //        http.authorizeRequests()
        //            .antMatchers("/securityNone")
        //            .permitAll()
        //            .anyRequest()
        //            .authenticated()
        //            .and()
        //            .httpBasic()
        
        //        http.csrf().disable()
        //            .cors()
        //            .and()
        //            .authorizeHttpRequests()
        //            .antMatchers("/**")
        //            .permitAll()
        //            .anyRequest().authenticated();
        //
        //        http.csrf()
        //            .disable()
        //            .authorizeRequests()
        //            .antMatchers(HttpMethod.DELETE)
        //            .hasRole("ADMIN")
        //            .antMatchers("/admin/**")
        //            .hasAnyRole("ADMIN")
        //            .antMatchers("/user/**")
        //            .hasAnyRole("USER", "ADMIN")
        //            .antMatchers("/login/**")
        //            .anonymous()
        //            .anyRequest()
        //            .authenticated()
        //            .and()
        //            .httpBasic()
        //            .and()
        //            .sessionManagement()
        //            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        return http.build();
    }
}
