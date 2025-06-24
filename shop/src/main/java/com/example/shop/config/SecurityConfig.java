package com.example.shop.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity //true가 되면 bean으로 등록하고 test에서 암호화할때 가져다 쓸수있음
@Slf4j
public class SecurityConfig {

    @Bean
    //SecurityFilterChain이 반환타입이 되는거고, bean에 등록을 하고 객체생성됨
    //예전에는 상속을 받아서 작성했었는데 지금은 바뀌었음
    //SecurityFilterChain을 쓰는게 최신권장방식
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //람다식으로 작성해야함
        log.info("-------Security Filter Chain-------");

        http
                .authorizeHttpRequests(
                        config -> config
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/", "/members/**", "/item/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                );

        http
                .csrf(csrf->csrf.disable()) //csrf 기능 끄는 설정

                .formLogin(
                        form->form.loginPage("/members/login")
                                .defaultSuccessUrl("/")
                                .usernameParameter("email") //login화면에서 name=username이면 생략가능
                                                            //username대신에 email을 쓰기 때문에 반드시 기입해야함
                                .failureUrl("/members/login/error")
                )
                .logout(logout-> logout
                        .logoutUrl("/members/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // 세션 무효화 (선택 사항이지만 일반적으로 사용)
                        .deleteCookies("JSESSIONID") // 쿠키 삭제 (선택 사항이지만 일반적으로 사용)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
