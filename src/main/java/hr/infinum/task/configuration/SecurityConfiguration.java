package hr.infinum.task.configuration;

import hr.infinum.task.security.TokenAuthenticationEntryPoint;
import hr.infinum.task.security.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;

  private final TokenAuthenticationFilter tokenAuthenticationFilter;

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().disable()
        .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
        .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
        .antMatchers(HttpMethod.GET, "/api/v1/cities").permitAll()
        .antMatchers("/api/**").authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(tokenAuthenticationEntryPoint)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

}
