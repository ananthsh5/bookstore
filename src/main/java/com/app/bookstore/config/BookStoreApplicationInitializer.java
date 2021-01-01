package com.app.bookstore.config;

import com.app.bookstore.BookStoreAppLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Ananth Shanmugam
 * Class to define configuration for spring boot
 */
@SpringBootApplication
public class BookStoreApplicationInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BookStoreAppLauncher.class);
    }

    @Bean(name = "bCryptPasswordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {		/* password encoder instance for spring security */
        return new BCryptPasswordEncoder();
    }
}
