package org.oleggalimov.dbmonitoring.back.configuration.security;

import org.oleggalimov.dbmonitoring.back.enumerations.Role;
import org.oleggalimov.dbmonitoring.back.services.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {
    private MongoUserDetailsService mongoUserDetailsService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConf(MongoUserDetailsService detailsService, BCryptPasswordEncoder encoder) {
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
        final String[] userAdminUrls = new String[]{
                "/create/user/**",
                "/delete/user/**",
                "/list/user/**",
                "/update/user/**"
        };
        final String[] instanceAdminUrls = new String [] {
                "/create/instance/**",
                "/delete/instance/**",
                "/update/instance/**"
        };
        final String loginProcessIngUrl = "/login";

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
//                .antMatchers(userAdminUrls).hasAuthority(Role.USER_ADMIN.name())
//                .antMatchers(instanceAdminUrls).hasAuthority(Role.ADMIN.name())
//                .loginProcessingUrl(loginProcessIngUrl)
        ;
    }
}
