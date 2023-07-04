/**
 * This configuration disables the default protection of CSRF (Cross-Site Request Forgery) attacks.
 * The attack triggers a malicient request to an API from a foreign website, e.g. an implicit GET
 * request from an img tag. If the endpoint call is secured by a session cookie, the request will
 * pass, as cookies are valid browser wide. Description of attack:
 * https://www.baeldung.com/spring-security-csrf Description of CSRF disabling in spring:
 * https://stackoverflow.com/a/52363615 Spring MVC by default protects to the attack by including a
 * secret token into the server sided generated page (which is then included on requests to the
 * API). Foreign malicious websites cannot have this token integrated, therefore requests fired from
 * those are not authenticated by token, and rejected by the backend API. There are two major
 * reasons to deliberately deactivate this protection: A) The API caller is not a browser, hence
 * CSRF is not an attack scenario to consider (not the case here, reserouce owner's user agent is a
 * browser), B) There is need for protecting tokens, because the API is already secured by other
 * means (this is likewise not the case here, as the OAuth tokens protect the Resource Server, not
 * the Client API). By concusion this configuration might be obsolete.
 */

package github.m5c.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  // TODO: figure out if obsolete.
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic().disable().csrf().disable();
  }
}
