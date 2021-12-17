package guru.springframework.config.security;

import guru.springframework.config.security.filter.CustomAuthenticationFilter;
import guru.springframework.config.security.filter.CustomAuthorizationFilter;
import guru.springframework.domain.security.Permissions;
import guru.springframework.services.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static guru.springframework.domain.security.Permissions.*;
import static guru.springframework.domain.security.Permissions.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String OPEN_ENDPOINT_A = "/api/login";
    public static final String OPEN_ENDPOINT_B = "/api/token/refresh";

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), tokenService);
        customAuthenticationFilter.setFilterProcessesUrl(OPEN_ENDPOINT_A); //This will customize the login path from SpringSecurity

        http.csrf().disable(); //Cross site request forgery deactivation
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //deactivating session

        //Open end-points:
        http.authorizeRequests().antMatchers(
                OPEN_ENDPOINT_A+"/**", OPEN_ENDPOINT_B + "/**")
                .permitAll();

        //GET, PUT, PATCH requests
        http.authorizeRequests().antMatchers(GET, "/api/**").hasAnyAuthority(
                USER.toString(), MANAGER.toString(), ADMIN.toString());
        http.authorizeRequests().antMatchers(PUT, "/api/**").hasAnyAuthority(
                USER.toString(), MANAGER.toString(), ADMIN.toString());
        http.authorizeRequests().antMatchers(PATCH, "/api/**").hasAnyAuthority(
                USER.toString(), MANAGER.toString(), ADMIN.toString());
        //POST and DELETE requests
        http.authorizeRequests().antMatchers(POST, "/api/**").hasAnyAuthority(MANAGER.toString());
        http.authorizeRequests().antMatchers(DELETE, "/api/**").hasAnyAuthority(ADMIN.toString());

        http.authorizeRequests().anyRequest().authenticated(); //This makes all requests to be authenticated
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(tokenService), UsernamePasswordAuthenticationFilter.class); //must be before all others filters
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", "/","index",
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
