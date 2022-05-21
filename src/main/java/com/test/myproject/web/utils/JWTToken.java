package com.test.myproject.web.utils;

import java.util.Date;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTToken {
    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.access.token}")
    private Integer expirationAccessToken = 24;

    @Value("${jwt.expiration.refresh.token}")
    private Integer expirationRefreshToken = 48;

    public JWTToken() {
        if (this.getSecretKey() == null) {
            this.setSecretKey("mystrongsecretkey");
        }

        if (this.getIssuer() == null) {
            this.issuer = "http://localhost:8087/,https://myproject.grope.site/";
        }
    }

    public Token generateToken(User user, TokenParameter tokenParameter) {
        Algorithm algorithm = Algorithm.HMAC256(this.getSecretKey().getBytes());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationAccessToken * 60 * 60 * 1000))
                .withIssuer(this.getIssuer())
                .withClaim("roles",
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .withClaim("lat", tokenParameter.getLat())
                .withClaim("lon", tokenParameter.getLon())
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationRefreshToken * 60 * 60 * 1000))
                .withIssuer(this.getIssuer())
                .sign(algorithm);

        return new Token(accessToken, refreshToken);
    }

    public DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(this.getSecretKey().getBytes());

        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }
}
