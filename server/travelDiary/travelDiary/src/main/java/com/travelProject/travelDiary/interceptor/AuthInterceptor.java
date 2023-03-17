package com.travelProject.travelDiary.interceptor;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${spring.secretKey}")
    private String secretKey;
    @Value("${spring.cookie.name}")
    private String cookieName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // cookie가 존재하고 해당 cookie의 jwt가 유효한지 확인 하여 userId값을 추출
        if(request.getCookies() != null){
            Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(this.cookieName)).findFirst().orElse(null);
            if(cookie != null){
                String jwtToken = cookie.getValue();
                Claims claims = this.getClaims(jwtToken);

                if(claims == null) return true;

                User user = User.builder().id(claims.get("id").toString()).build();
                request.setAttribute("user",user);
            }
        }
        return true;
    }

    private Claims getClaims(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(this.secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            // 토큰 유효성 확인
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
//            throw new exceptionCode(ErrorCode.INVALID_JWT_ERROR);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
//            throw new exceptionCode(ErrorCode.INVALID_JWT_ERROR);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
//            throw new exceptionCode(ErrorCode.INVALID_JWT_ERROR);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
//            throw new exceptionCode(ErrorCode.INVALID_JWT_ERROR);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
//            throw new exceptionCode(ErrorCode.INVALID_JWT_ERROR);
        } catch (SignatureException e) {
            log.info("JWT signature does not match locally computed signature");
        }
        return null;
    }
}
