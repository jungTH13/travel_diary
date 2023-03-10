package com.travelProject.travelDiary.interceptor;

import com.travelProject.travelDiary.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private Environment env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // cookie가 존재하고 해당 cookie의 jwt가 유효한지 확인 하여 userId값을 추출
        if(request.getCookies() != null){
            Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("travel_diary")).findFirst().orElse(null);
            if(cookie != null){
                String jwtToken = cookie.getValue();
                Claims claims = this.getClaims(jwtToken);

                User user = User.builder().id(claims.get("id").toString()).build();

                if(claims != null) request.setAttribute("user",user);
            }

        }

        return true;
    }

    private Claims getClaims(String token){
        try{
            return Jwts.parser()
                    .setSigningKey("travel_diary")
                    .parseClaimsJws(token)
                    .getBody();
            // 토큰 유효성 확인
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
//            throw new CustomJwtRuntimeException();
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
//            throw new CustomJwtRuntimeException();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
//            throw new CustomJwtRuntimeException();
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
//            throw new CustomJwtRuntimeException();
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
//            throw new CustomJwtRuntimeException();
        }
        return null;
    }
}
