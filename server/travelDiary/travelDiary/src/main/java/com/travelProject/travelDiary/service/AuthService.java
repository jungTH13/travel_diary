package com.travelProject.travelDiary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.GoogleUserInfo;
import com.travelProject.travelDiary.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthService {

    @Value("${google.OAuth.clientId}")
    private String clientId;

    @Value("${google.OAuth.clientSecret}")
    private String clientSecret;

    private String grantType = "authorization_code";

    @Value("${google.OAuth.redirectUri}")
    private String redirectUri;

    public User validateJWTUser(HttpServletRequest request){
        User user = (User) request.getAttribute("user");

        if(user == null || user.getId() == null) throw new exceptionCode(ErrorCode.INVALID_USER_PARAMETER);

        return user;
    }

    public String getGoogleToken(String code)throws  IOException{
        String targetUrl = "https://oauth2.googleapis.com/token";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("client_id",clientId);
        body.put("client_secret",clientSecret);
        body.put("grant_type",grantType);
        body.put("redirect_uri",redirectUri);
        body.put("code",code);

        HttpEntity<Map> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(targetUrl,HttpMethod.POST, request, Map.class);

        return response.getBody().get("access_token").toString();
    }

    public GoogleUserInfo getGoogleUserInfo(String token) throws IOException {
        String targetUrl = "https://www.googleapis.com/oauth2/v2/userinfo";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+token);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, Map.class);
        Map<String, String> result = response.getBody();

        GoogleUserInfo googleUserInfo = new GoogleUserInfo(result.get("email"),result.get("picture"));

        return googleUserInfo;
    }

}
