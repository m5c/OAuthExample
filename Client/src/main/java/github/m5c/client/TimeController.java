package github.m5c.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
public class TimeController {

  @RequestMapping(value = "/time", method = RequestMethod.GET)
  public ModelAndView getTime() {
    ModelAndView modelAndView = new ModelAndView("getTime");

    return modelAndView;
  }


  @RequestMapping(value = "/showTime", method = RequestMethod.GET)
  public ModelAndView showTime(@RequestParam("code") String code) throws JsonProcessingException {
    ResponseEntity<String> response = null;
    System.out.println("Authorization Code------" + code);
    RestTemplate restTemplate = new RestTemplate();
    String credentials = "timeservice-client:password-for-timeservice-client";
//    String encodedCredentials = new String(Base64.encodeBase64(credentialsByteArray));
    String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.add("Authorization", "Basic " + encodedCredentials);

    HttpEntity<String> request = new HttpEntity<String>(headers);
    String access_token_url = "http://localhost:8080/oauth/token";
    access_token_url += "?code=" + code;
    access_token_url += "&grant_type=authorization_code";
    access_token_url += "&redirect_uri=http://localhost:8090/showTime";

    response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);

    System.out.println("Access Token Response ---------" + response.getBody());

    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(response.getBody());
    String token = node.path("access_token").asText();

    String url = "http://localhost:8084/api/time";

    HttpHeaders headers1 = new HttpHeaders();
    headers1.add("Authorization", "Bearer" + token);
    HttpEntity<String> entity = new HttpEntity<>(headers1);

    ResponseEntity<String> r = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    System.out.println("Time is " + r.getBody());

    ModelAndView modelAndView = new ModelAndView("displayTime");
    modelAndView.addObject("time", r.getBody());

    return modelAndView;
  }
}
