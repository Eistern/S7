package com.example.democlient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class DemoClientApplication {
    private static final String URL_LOGIN = "http://localhost:8080/user/login";
    private static final String URL_GET_PERSON = "http://localhost:8080/person/{id}";

    public static void main(String[] args) throws InterruptedException {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate connector = builder.build();
        testGetPerson(authorize("admin", "password", connector), connector);
        testGetPerson(authorize("TesterSpeedy", "passwordSpeedy", connector), connector);
    }

    private static String authorize(String username, String password, RestTemplate connector) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<LoginForm> requestForLogin = new HttpEntity<>(new LoginForm(username, password), headers);
        ResponseEntity<TokenResponse> responseToken = connector.exchange(URL_LOGIN, HttpMethod.POST, requestForLogin, TokenResponse.class);
        assert responseToken.getBody() != null;
        return responseToken.getBody().getToken();
    }

    private static void testGetPerson(String token, RestTemplate connector) throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + token); //Add auth header
        HttpEntity<String> requestForGet = new HttpEntity<>(headers); //Create request with given headers and no body


        for (int id = 0; id < 10; id++) {
            try {
                ResponseEntity<Person> response = connector.exchange(URL_GET_PERSON, HttpMethod.GET, requestForGet, Person.class, Integer.toString(id));
                if (response.getStatusCode().equals(HttpStatus.OK))
                    System.out.println(response.getBody());
            }
            catch (HttpClientErrorException e) {
                System.err.println(e.getMessage());
            }
            Thread.sleep(1000);
        }
    }
}
