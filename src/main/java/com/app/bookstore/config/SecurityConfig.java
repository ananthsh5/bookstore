package com.app.bookstore.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Ananth Shanmugam
 * Class to define security configuration for spring boot
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${bookstore.users-query}")
    private String usersQuery;

    @Value("${bookstore.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	 /* configure the ignore folders for spring security */
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {  
    	/* configure the matchers for spring security */
        http.authorizeRequests()
            .antMatchers("/", "/account/**", "/register/**", "/h2-console/**").permitAll()
            .antMatchers("/admin/**").hasAuthority("ADMIN")
            .antMatchers("/publisher/**").hasAuthority("PUBLISHER")
            .antMatchers("/customer/**").hasAuthority("CUSTOMER")
            .and()
            .formLogin()
            .loginPage("/account/login")
            .failureUrl("/account/login?error=true")
            .defaultSuccessUrl("/")
            .usernameParameter("email")
            .passwordParameter("password")
            .and()
            .rememberMe()
            .rememberMeParameter("remember-me")
            .rememberMeCookieName("remember-me")
            .tokenValiditySeconds(86400)
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/account/logout"))
            .logoutSuccessUrl("/")
            .and()
            .exceptionHandling()
            .accessDeniedPage("/403")
            .and()
            .csrf()
            .disable()
            .sessionManagement().maximumSessions(100)
            .expiredUrl("/account/login");

    }
    

    @Override
    public void configure(WebSecurity web) throws Exception {  
    	/* configure the ignore folders for spring security */
        web.ignoring()
           .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/h2-console/**");
    }
}
