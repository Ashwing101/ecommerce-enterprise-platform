package com.ecommerce.project.security.jwt;
import com.ecommerce.project.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;




@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${spring.app.jwtSecret}")
    private  String  jwtSecret;

    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;


    //Getting JWT From Header
//        public  String getJwtFromHeader(HttpServletRequest request){
//            String bearerToken = request.getHeader("Authorization");
//            logger.info("Authorization Header: {}", bearerToken);
//            if(bearerToken != null && bearerToken.startsWith("Bearer ")){
//                return  bearerToken.substring(7);
//            }
//            return null;
//        }


    //Getting JWT From Cookie
    public String getjwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie != null){
               return cookie.getValue();
            }
            return  null;


    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails){
        String jwt = generateTokenFromUsername(userDetails.getUsername());
        ResponseCookie responseCookie  = ResponseCookie.from(
                jwtCookie,jwt).path("/api")
                .maxAge(24 * 60 * 60)
                .httpOnly(false)
                .build();
        return  responseCookie;


    }

    public ResponseCookie generateGetCleanJwtCookie(){
        ResponseCookie responseCookie  = ResponseCookie.from(
                        jwtCookie,null).path("/api")
                .build();
        return  responseCookie;


    }

        //Generating Token from Username
        public  String  generateTokenFromUsername(String username){
               // String userName = userDetails.getUsername();
                System.out.println(username + " User Name");
                return Jwts.builder()
                        .subject(username)
                        .issuedAt(new Date())
                        .expiration(new Date((new Date().getTime() + jwtExpirationMs)))
                        .signWith(key()).
                        compact();
        }



        public  String getUserNameFromJWTToken(String token){
            return Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(token)
                    .getPayload().getSubject();
        }

        public Key key(){
            return Keys.hmacShaKeyFor(
                    Decoders.BASE64.decode(jwtSecret)
            );
        }

        //Validate JWT Token
    public  boolean validateJwtToken(String authToken){
            try{
                System.out.println("Validate");
                Jwts.parser()
                        .verifyWith((SecretKey) key())
                        .build()
                        .parseSignedClaims(authToken);
                return  true;
            }catch (MalformedJwtException exception){
                    logger.error("Invalid JWT {}" + exception);
            }catch (ExpiredJwtException exception){
                logger.error("JWT token is expired {}" + exception);
            }catch (UnsupportedJwtException exception){
                logger.error("JWT token is unsupported {}" + exception);
            }catch (IllegalArgumentException exception){
                logger.error("JWT claims string is empty: {}" + exception);
            }
            return  false;
        }
}
