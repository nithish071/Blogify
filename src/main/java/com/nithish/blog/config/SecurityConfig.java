package com.nithish.blog.config;

import com.nithish.blog.security.JWTAuthenticationEntryPoint;
import com.nithish.blog.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@Configuration
@EnableMethodSecurity
@EnableWebMvc
@EnableWebSecurity
public class SecurityConfig{

	public static final String[] PUBLIC_URLS = {
			"/api/v1/auth/**",
			"/v3/api-docs",
			"/v2/api-docs",
			"/swagger-resources/**",
			"/swagger-ui/**",
			"/webjars/**"
	};
	@Autowired
	private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private UserDetailsService userDetailService;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


	        http.csrf(AbstractHttpConfigurer::disable)
					.authorizeHttpRequests((authz) -> authz
							.requestMatchers(PUBLIC_URLS).permitAll().requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll().requestMatchers(HttpMethod.POST,"/api/users/create").permitAll().requestMatchers(HttpMethod.GET).permitAll().anyRequest().authenticated()
					)
					.httpBasic(Customizer.withDefaults()).exceptionHandling(
							exceptionConfigurer -> exceptionConfigurer
									// Global exception handling for all authentication errors (consider more specific handlers)
									.authenticationEntryPoint(this.jwtAuthenticationEntryPoint)).sessionManagement( sessionManagement->
							sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

			http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

			http.authenticationProvider(daoAuthenticationProvider());

			return http.build();
	    }

    
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(this.customerUserDetailService).passwordEncoder(passwordEncoder());
//	}
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(){
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
