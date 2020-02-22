package org.oleggalimov.dbmonitoring.back.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConf extends WebSecurityConfigurerAdapter {
    private UserDetailsService mongoUserDetailsService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConf(UserDetailsService detailsService, BCryptPasswordEncoder encoder) {
        this.mongoUserDetailsService = detailsService;
        this.passwordEncoder = encoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(mongoUserDetailsService)
                .passwordEncoder(passwordEncoder)
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
                .and()
                .httpBasic()
//                .antMatchers(userAdminUrls).hasAuthority(Role.USER_ADMIN.name())
//                .antMatchers(instanceAdminUrls).hasAuthority(Role.ADMIN.name())
//                .loginProcessingUrl(loginProcessIngUrl)
        ;
    }
}
