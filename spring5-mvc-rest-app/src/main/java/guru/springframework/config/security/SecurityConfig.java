package guru.springframework.config.security;

import guru.springframework.config.security.filter.CustomAuthenticationFilter;
import guru.springframework.config.security.filter.CustomAuthorizationFilter;
import guru.springframework.domain.security.Level;
import guru.springframework.services.security.TokenService;
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

    public static final String OPEN_ENDPOINT_A = "/api/login";
    public static final String OPEN_ENDPOINT_B = "/api/token/refresh";
    public static final String OPEN_ENDPOINT_C = "/swagger-ui";

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

        http.csrf().disable(); //Cross site request forgery
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //Open end-points:
        http.authorizeRequests().antMatchers(OPEN_ENDPOINT_A+"/**",
                OPEN_ENDPOINT_B + "/**", OPEN_ENDPOINT_C +"/**")
                .permitAll();


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
        http.addFilterBefore(new CustomAuthorizationFilter(tokenService), UsernamePasswordAuthenticationFilter.class); //must be before all others filters
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
