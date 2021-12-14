package guru.springframework.security;

import guru.springframework.domain.security.Level;
import guru.springframework.security.filter.CustomAuthenticationFilter;
import guru.springframework.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login"); //This will customize the login path from SpringSecurity

        http.csrf().disable(); //Cross site request forgery
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //Open entry requests:
        http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh/**").permitAll();

        //GET, PUT, PATCH requests
        http.authorizeRequests().antMatchers(GET, "/api/**").hasAnyAuthority(
                Level.USER.toString(),Level.MANAGER.toString(),Level.ADMIN.toString());
        http.authorizeRequests().antMatchers(PUT, "/api/**").hasAnyAuthority(
                Level.USER.toString(),Level.MANAGER.toString(),Level.ADMIN.toString());
        http.authorizeRequests().antMatchers(PATCH, "/api/**").hasAnyAuthority(
                Level.USER.toString(),Level.MANAGER.toString(),Level.ADMIN.toString());
        //POST and DELETE requests
        http.authorizeRequests().antMatchers(POST, "/api/**").hasAnyAuthority(Level.MANAGER.toString());
        http.authorizeRequests().antMatchers(DELETE, "/api/**").hasAnyAuthority(Level.MANAGER.toString());

        http.authorizeRequests().anyRequest().authenticated(); //This makes all requests to be authenticated
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class); //must be before all others filters
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
