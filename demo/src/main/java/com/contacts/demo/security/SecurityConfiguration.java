package com.contacts.demo.security;

import com.contacts.demo.security.data.types.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;
    private final UserInfoService userInfoService;

    @Autowired
    public SecurityConfiguration(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DataSource dataSource, UserInfoService userInfoService) {
        this.dataSource = dataSource;
        this.userInfoService = userInfoService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                userDetailsService(userInfoService).
                passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                httpBasic().
                and().
                csrf().disable().
                authorizeRequests().
                    antMatchers("/user/list").hasAuthority(Role.Roles.ADMIN_ROLE.toString()).
                    antMatchers("/", "/user/**").permitAll().
                    anyRequest().authenticated();
    }
}
