package ru.hard2code.gisdbapi.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Objects;

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
                        authorizeRequests.requestMatchers(env.getProperty("app.rest.api.prefix") + "/**")
                                .hasRole("USER")
                                .anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.builder()
                .username(Objects.requireNonNull(env.getProperty("app.rest.basic-auth.user.name")))
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(Objects.requireNonNull(env.getProperty("app.rest.basic-auth.user.password"))))
                .roles("USER")
                .build();

        var admin = User.builder()
                .username(Objects.requireNonNull(env.getProperty("app.rest.basic-auth.admin.name")))
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(Objects.requireNonNull(env.getProperty("app.rest.basic-auth.admin.password"))))
                .roles("USER, ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

}
