package com.capstone.unwind.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Data
public class SecurityConfiguration {

    @Autowired
    private final JwtAuthFilter jwtAuthFilter;
    @Autowired
    private final AuthenticationProvider authenticationProvider;
    @Autowired
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

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
                                .requestMatchers("/api/customer/room/**")
                                .hasAnyAuthority("CUSTOMER", "TIMESHARECOMPANYSTAFF")
                                .requestMatchers("/api/wallet/**")
                                .hasAnyAuthority("CUSTOMER", "TIMESHARECOMPANY","SYSTEMSTAFF")
                                .requestMatchers("api/customer/**")
                                .hasAuthority("CUSTOMER")
                                .requestMatchers("api/timeshare-company/**")
                                .hasAuthority("TIMESHARECOMPANY")
                                .requestMatchers("/api/public/**")
                                .permitAll()
                                .requestMatchers("/api/payment/**")
                                .permitAll()
                                .requestMatchers("api/payment/payment-info")
                                .permitAll()
                                .requestMatchers("api/test/tsStaff")
                                .hasAuthority("TIMESHARECOMPANYSTAFF")
                                .requestMatchers("api/system-staff/**")
                                .hasAuthority("SYSTEMSTAFF")
                                .requestMatchers("/s3/file/**")
                                .permitAll()
                                .requestMatchers("/api/email/**")
                                .permitAll()
                                .requestMatchers("api/timeshare-staff/**")
                                .hasAuthority("TIMESHARECOMPANYSTAFF")
                                .requestMatchers("api/test/api/some-endpoint")
                                .hasAuthority("TIMESHARECOMPANYSTAFF")
                                .requestMatchers("/api/auth/oauth2-login", "/api/auth/oauth2-success")
                                .permitAll()
                                .anyRequest()
                                .authenticated())

                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/api/auth/oauth2-success", true)
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        return http.build();
    }
}
