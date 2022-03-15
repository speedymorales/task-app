package com.taskapp.taskapp.services;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.taskapp.taskapp.customObjects.AuthenticationObj;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public AuthenticationObj hashPassword(String password) {
        try {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");  
        byte[] hash = factory.generateSecret(spec).getEncoded();
        String stringPassword = new String(Base64.encodeBase64(hash));
        String stringSalt = new String(Base64.encodeBase64(salt));
        AuthenticationObj authenticationObj = new AuthenticationObj();
        authenticationObj.password = stringPassword;
        authenticationObj.salt = stringSalt;
        return authenticationObj;
        } catch (Exception e) {
            AuthenticationObj authObj = new AuthenticationObj();
            return authObj;
        }

    }

    public Boolean comparePassword(String password1, String password2, String salt) throws Exception {
            String decodedSalt = new String(Base64.decodeBase64(salt.getBytes()));
            byte[] saltHash = decodedSalt.getBytes();
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");  
            KeySpec spec2 = new PBEKeySpec(password2.toCharArray(), saltHash, 65536, 128);
            byte[] hash2 = factory.generateSecret(spec2).getEncoded();
            String hashedPassword = new String(Base64.encodeBase64(hash2));
            System.out.println(password1);           
            System.out.println(hashedPassword);           
            return password1.equals(hashedPassword);

    }
}
