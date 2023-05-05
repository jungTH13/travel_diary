package com.travelProject.travelDiary.dto;

import com.travelProject.travelDiary.entity.User;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String email;

    private String auth;

    private String picture;

    private Long loginCount;

    public UserInfo(User user){
        this.email = user.getEmail();
        this.auth = user.getAuth();
        this.picture = user.getPicture();
        this.loginCount = user.getLoginCount();
    }
}
