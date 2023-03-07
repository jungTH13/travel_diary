package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.repository.UserRepository;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServie {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment env;

    public String getNewUserJWT(){
        User user = createGuestUser();
        if(user == null) return null;

        Date now = new Date();
        String jwtToken = Jwts.builder()
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .setIssuer("travel")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofHours(24*365).toMillis()))
                .claim("id",user.getId())
                .signWith(SignatureAlgorithm.HS256,"travel_diary")
                .compact();

        return jwtToken;
    }

    public Cookie wrapDataAtCookie(String key,String value){
        Cookie cookie = new Cookie(key,value);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*365);
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        return cookie;
    }

    private User createGuestUser(){

        int count = 10;
        while(count-->0) {
            String uuid = UUID.randomUUID().toString();
            User user = userRepository.findById(uuid).orElse(null);
            if (user == null) {
                User newUser = User.builder().id(uuid).auth("guest").build();
                newUser =userRepository.save(newUser);
                return newUser;
            }
        }

        return null;
    }

}
