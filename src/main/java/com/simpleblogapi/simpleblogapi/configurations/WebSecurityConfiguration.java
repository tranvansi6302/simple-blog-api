package com.simpleblogapi.simpleblogapi.configurations;

import com.simpleblogapi.simpleblogapi.filters.JwtTokenFilter;
import com.simpleblogapi.simpleblogapi.models.Role;
import com.simpleblogapi.simpleblogapi.utils.ErrorResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    "api/v1/login",
                                    "api/v1/register"

                            )
                            .permitAll()
                            .requestMatchers(GET, "api/v1/categories/**").permitAll()
                            .requestMatchers(GET, "api/v1/posts/**").permitAll()


                            .requestMatchers(DELETE, "api/v1/categories/**").hasRole(Role.ADMIN)
                            .requestMatchers(POST, "api/v1/categories/**").hasRole(Role.ADMIN)
                            .requestMatchers(PATCH, "api/v1/categories/**").hasRole(Role.ADMIN)

                            .requestMatchers(PATCH, "api/v1/users/me").hasRole(Role.USER)
                            .requestMatchers(GET, "api/v1/users/me").hasRole(Role.USER)
                            .requestMatchers(PATCH, "api/v1/users/me/upload").hasRole(Role.USER)

                            .requestMatchers(POST, "api/v1/posts/**").hasRole(Role.USER)
                            .requestMatchers(PATCH, "api/v1/posts/**").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(POST, "/api/v1/posts/{\\d+}/upload").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(DELETE, "api/v1/posts/**").hasAnyRole(Role.ADMIN, Role.USER)

                            .requestMatchers(DELETE, "api/v1/users/**").hasRole(Role.ADMIN)
                            .requestMatchers(PATCH, "api/v1/users/**").hasRole(Role.ADMIN)


                            .anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                            ErrorResponseUtil.sendErrorResponse(
                                    response,
                                    HttpServletResponse.SC_FORBIDDEN,
                                    "Access denied"
                            );
                        })
                );
        return http.build();
    }
}