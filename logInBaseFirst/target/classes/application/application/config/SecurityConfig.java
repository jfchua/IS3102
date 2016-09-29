package application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .httpBasic().and()
        .authorizeRequests()
          .antMatchers("/**","https://localhost:8443","https://localhost:8443/","/index.html", "/#/login", "/index","/user/loginVerify","/").permitAll().anyRequest()
          .authenticated().and().logout().logoutUrl("//localhost:8443/logout").and()
        .csrf()
          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
     /*   http
			.authorizeRequests()
				.antMatchers("/", "/public/**","/reset","/resetPassword","/login","/#/users.html", "#/roles.html").permitAll()
				.antMatchers("/users/**").hasAuthority("ADMIN")
				.antMatchers("/users/**").hasAuthority("ADMIN")
				.anyRequest().fullyAuthenticated()
                .and()
            .formLogin()
                .loginPage("/login.html")
                .failureUrl("/404.html")
                .usernameParameter("email")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .deleteCookies("remember-me")
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
             .rememberMe();
    }*/

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

}



