package com.capstone.unwind.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Data
public class SecurityConfiguration {

    @Autowired
    private final JwtAuthFilter jwtAuthFilter;
    @Autowired
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests( auth ->
                        auth
                                .requestMatchers("api/auth/**")
                                .permitAll()
                                .requestMatchers("/v3/**")
                                .permitAll()
                                .requestMatchers("/swagger-ui/**")
                                .permitAll()
                                .requestMatchers("/swagger-ui.html")
                                .permitAll()
                                .requestMatchers("api/test/unsecured")
                                .permitAll()
                                .requestMatchers("api/admin/**")
                                .hasAuthority("ADMIN")
                                .requestMatchers("api/customer/**")
                                .hasAuthority("CUSTOMER")
                                .requestMatchers("api/timeshare-company/**")
                                .hasAuthority("TIMESHARECOMPANY")
                                .requestMatchers("/api/public/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
