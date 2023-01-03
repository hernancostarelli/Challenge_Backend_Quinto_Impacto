package com.api.challenge.config;

import com.api.challenge.service.impl.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AdminServiceImpl userDetailsService;
    //private final JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                        .antMatchers("/admin/*").hasRole("ADMIN")
                        //.antMatchers("/*").hasAnyRole("USUARIO,ADMIN")
                        .antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()
                .and().formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/index-admin")
                        .permitAll()
                .and().logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                .and().csrf().disable();
    }
}