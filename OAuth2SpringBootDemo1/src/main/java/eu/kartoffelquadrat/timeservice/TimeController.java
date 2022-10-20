/**
 * OAuth2 secured version of the timeservice. Following this tutorial: http://websystique.com/spring-security/secure-spring-rest-api-using-oauth2/
 *
 * @Author: Maximilian Schiedermeier
 * @Date: April 2019
 */
package eu.kartoffelquadrat.timeservice;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/***
 * This controller tells the time via a REST interface.
 */
@RestController
public class TimeController {

    /**
     * Gateway method to access service functionality - may be replaced by other implementations, depending the feature
     * selection.
     * <p>
     * Only registered users (adherent to the role "TIMEUSER" can access this resource). See Users defined
     * "OAuth2SecurityConfiguration".
     *
     * @return a String that encodes the current date and time.
     */
    @PreAuthorize("hasAuthority('ROLE_TIMEUSER')")
    @GetMapping("/api/time")
    public String getTime() {
        return TimeServiceUtils.lookUpCurrentTime();
    }

    /**
     * Dummy method to demonstrate that any resources without a "PreAuthorize" clause can be accessed as usually, even
     * if they math the "/api/**" URL pattern that enables the securitycontext in "ResourceServerConfiguration".
     *
     * @return a constant test string.
     */
    @GetMapping("/api/dummy")
    public String getUnprotectedSomething() {
        return "Here is a dummy string from an unprotected api endpoint.";
    }
}

