package com.gigilife.game.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import com.gigilife.game.security.jwt.JwtAuthenticationFilter;


@Configuration
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
			.headers((headers) -> headers
				.xssProtection(xss -> xss
					.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
				)
				.contentSecurityPolicy(cps -> cps
					.policyDirectives("script-src 'self' https://cdn.jsdelivr.net/npm/")
				)
			)
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/api/game/**").authenticated()
				.requestMatchers("/**").permitAll()
				.anyRequest().authenticated()
			)
			
			.csrf(AbstractHttpConfigurer::disable)

			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
			

		return http.build();
	}

	@Bean
	static PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}