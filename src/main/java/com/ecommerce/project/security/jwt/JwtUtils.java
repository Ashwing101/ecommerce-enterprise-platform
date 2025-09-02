package com.ecommerce.project.security.jwt;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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

    //Getting JWT From Header
        public  String getJwtFromHeader(HttpServletRequest request){
            String bearerToken = request.getHeader("Authorization");
            logger.info("Authorization Header: {}", bearerToken);
            if(bearerToken != null && bearerToken.startsWith("Bearer ")){
                return  bearerToken.substring(7);
            }
            return null;
        }

        //Generating Token from Username
        public  String generateTokenFromUsername(UserDetails userDetails){
                String userName = userDetails.getUsername();
                return Jwts.builder()
                        .subject(userName)
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
