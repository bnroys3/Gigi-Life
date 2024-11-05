package com.gigilife.game.security.jwt;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.gigilife.game.fileloader.FileLoader;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;


@Component
public class JwtUtil {

    @Autowired
    private FileLoader fileLoader;

    private String privateKeyPath = "classpath:keys/PrivateJwtKey.txt";
    private String publicKeyPath = "classpath:keys/PublicJwtKey.txt";

    //file loader used for private and public keys
    public JwtUtil(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    //build jwt given params
    public String generateAccessToken(String username, List<String> roles, long expirationTime) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        
        return Jwts.builder()
                .subject(username)
                .claim("roles",roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(generateJwtKeyEncryption())
                .compact();
    }


    //encrypt jwt using private key
    public PrivateKey generateJwtKeyEncryption() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        String jwtPrivateKey = fileLoader.readKeyFromFile(privateKeyPath);

        byte[] keyBytes = Base64.getDecoder().decode(jwtPrivateKey);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);

        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    //decrypt jwt using public key
    public PublicKey generateJwtKeyDecryption() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {

        
        String jwtPublicKey = fileLoader.readKeyFromFile(publicKeyPath);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        byte[] keyBytes = Base64.getDecoder().decode(jwtPublicKey);

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    //extract payload of token
    public Claims extractClaims(String token) {
        try {
             Object claims = Jwts.parser().verifyWith(generateJwtKeyDecryption()).build().parse(token).getPayload();
             if(claims instanceof Claims) {
                return (Claims)claims;
             }
             return null;
        } catch (Exception e) {
            return null;
        }
    }

    //Return authentication token with appropriate authorities
    public Authentication getAuthentication(String token) {

        Claims claims = extractClaims(token);
        
        String username = getUsername(claims);
        if(username == null){
            return null;
        }

        List<String> roles = getRoles(claims);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return new UsernamePasswordAuthenticationToken(username, null, authorities);

    }

    //determine username from claims
    public String getUsername(Claims claims) {

        if(claims == null) {
            return null;
        }
        return claims.getSubject();

    }

    //determine whether token is expired from claims
    public boolean isExpired(Claims claims) {

        if(claims == null) {
            return true;
        }

        Date expiresAt = claims.getExpiration();
        return expiresAt.before(new Date());
    }

    //extract roles from claims
    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims) {

        if(claims == null) {
            return null;
        }

        return claims.get("roles", List.class);
        
    }

    //Generate refresh token
    public String getRefreshedToken(String refreshToken, long expirationTime) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Claims claims = extractClaims(refreshToken);
        if(isExpired(claims)) {
            return null;
        }
        return generateAccessToken(getUsername(claims), getRoles(claims), expirationTime);
    }


}
