/**
 * Original source: http://websystique.com/spring-security/secure-spring-rest-api-using-oauth2/
 * (Carefull with this guide, it claims to use the "password grant", which is not listed in official
 * grant type list. Unsure if reliable.
 */

package github.m5c.resourceserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  private static final String RESOURCE_ID = "my_rest_api";

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId(RESOURCE_ID).stateless(false);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {

    // Enable httpSecurity for all URLs that start with /api/... This allows access to the security context and
    // enables further access protection with "PreAuthorize" annotations. See TimeController.
    http.authorizeRequests().antMatchers("/api/**")
        .permitAll() // Allow by default all api access to go unauthenticated
    ;
  }

}