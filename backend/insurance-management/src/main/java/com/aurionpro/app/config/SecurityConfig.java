package com.aurionpro.app.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aurionpro.app.security.JwtAuthenticationEntryPoint;
import com.aurionpro.app.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
//	@Autowired
//	private UserDetailsService userDetailsService;
	
	@Bean
	static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	  SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
		http.csrf(csrf -> csrf.disable()).cors(withDefaults());
	    http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
	    
	    http.authorizeHttpRequests(request -> request.requestMatchers("/app/register/**").permitAll());
	    http.authorizeHttpRequests(request -> request.requestMatchers("/app/login").permitAll());
	    http.authorizeHttpRequests(request -> request.requestMatchers("/app/password/forgot-password").permitAll());
	    http.authorizeHttpRequests(request -> request.requestMatchers("/app/password/reset-password").permitAll());
	    http.authorizeHttpRequests(request -> request.requestMatchers("/app/documents/**").permitAll());
	   

	    http.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.GET, "/app/"));
	    http.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, "/app/"));
	    http.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.PUT, "/app/"));
	    http.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.DELETE, "/app/"));
	    http.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint));
	    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    http.authorizeHttpRequests(request -> request.anyRequest().authenticated());
	    return http.build();
	  }
}
