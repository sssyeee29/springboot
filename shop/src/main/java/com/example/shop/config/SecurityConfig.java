package com.example.shop.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //true가 되면 bean으로 등록하고 test에서 암호화할때 가져다 쓸수있음
@Slf4j
public class SecurityConfig {

    @Bean
    //SecurityFilterChain이 반환타입이 되는거고, bean에 등록을 하고 객체생성됨
    //예전에는 상속을 받아서 작성했었는데 지금은 바뀌었음
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("-------Security Filter Chain-------");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
