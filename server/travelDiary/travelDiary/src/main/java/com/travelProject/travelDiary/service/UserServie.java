package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.UUID;

@Service
public class UserServie {

    @Autowired
    private UserRepository userRepository;

    public Cookie getNewGestCookie(){
        User user = createGuestUser();
        if(user == null) return null;

        Cookie cookie = new Cookie("travel-guest",user.getId().toString());
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
