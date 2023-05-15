package ru.hard2code.gisdbapi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class AuthorizeUrlsSecurityConfig {

    private final Environment env;


    public AuthorizeUrlsSecurityConfig(Environment env) {
        this.env = env;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic(withDefaults())
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests.requestMatchers(env.getProperty("app.rest.api-prefix", "/api") + "/**")
                                .hasAnyRole("USER", "ADMIN")
                                .anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.builder()
                .username(env.getProperty("app.rest.auth.user.name", "user"))
                .password(passwordEncoder().encode(env.getProperty("app.rest.auth.user.password", "password")))
                .roles("USER")
                .build();

        var admin = User.builder()
                .username(env.getProperty("app.rest.auth.admin.name", "admin"))
                .password(passwordEncoder().encode(env.getProperty("app.rest.auth.admin.password", "password")))
                .roles("USER, ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
