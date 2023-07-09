package com.example.techshopserver.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableMethodSecurity
class SecurityConfig(private val jwtTokenFilter: JwtTokenFilter) {

    @Bean
    fun filterChain(security: HttpSecurity): DefaultSecurityFilterChain =
        security.cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS).and()
            .exceptionHandling()
            .authenticationEntryPoint { _, res, ex -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.message) }
            .and()
            .headers { it.frameOptions().disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(HttpMethod.GET).permitAll()
                    .requestMatchers("/error/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter::class.java)
            .build()

    @Bean
    fun corsFilter() =
        CorsConfiguration().apply {
            addAllowedHeader("*")
            addAllowedOrigin("*")
            addAllowedMethod("*")
        }.let { corsConfiguration ->
            UrlBasedCorsConfigurationSource().apply {
                registerCorsConfiguration("/**", corsConfiguration)
            }
        }.let { urlBasedCorsConfigSrc -> CorsFilter(urlBasedCorsConfigSrc) }
}