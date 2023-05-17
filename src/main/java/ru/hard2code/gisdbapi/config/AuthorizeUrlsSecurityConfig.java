package ru.hard2code.gisdbapi.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
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

    @Value("${app.rest.api-prefix:/api}")
    private String apiPrefix;


    public AuthorizeUrlsSecurityConfig(Environment env) {
        this.env = env;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic(withDefaults())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(HttpMethod.GET).hasAuthority(
                                        "read")
                                .requestMatchers(HttpMethod.POST).hasAuthority(
                                        "write")
                                .requestMatchers(HttpMethod.PUT).hasAuthority(
                                        "write")
                                .requestMatchers(HttpMethod.PATCH).hasAuthority(
                                        "write")
                                .requestMatchers(HttpMethod.DELETE).hasAuthority(
                                        "write")
                                .anyRequest()
                                .authenticated());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.builder()
                .username(env.getProperty("app.rest.auth.user.name",
                        "user"))
                .password(passwordEncoder().encode(env.getProperty("app" +
                        ".rest.auth.user.password", "password")))
                .roles("USER")
                .authorities("read")
                .build();

        var admin = User.builder()
                .username(env.getProperty("app.rest.auth.admin.name",
                        "admin"))
                .password(passwordEncoder().encode(env.getProperty("app" +
                        ".rest.auth.admin.password", "password")))
                .roles("ADMIN")
                .authorities("read", "write")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
