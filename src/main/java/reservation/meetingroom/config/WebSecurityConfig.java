package reservation.meetingroom.config;

import reservation.meetingroom.common.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${auth.header}")
    private String HEADER;

    @Value("${auth.prefix}")
    private String BEARER;

    @Value("${application.api.url}")
    private String API_URL;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/v1/token").permitAll()
            .anyRequest().authenticated()
            .and()
                .addFilterBefore(getJWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**")
            .antMatchers("/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**");
    }

    private JWTAuthorizationFilter getJWTAuthorizationFilter() {
        return JWTAuthorizationFilter.builder()
                .HEADER(HEADER)
                .BEARER(BEARER)
                .API_URL(API_URL)
                .build();
    }
}