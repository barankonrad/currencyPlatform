package sample.gui.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashData{
    public static String encodeHash(String password, int salt){
        String salted = password + salt;
        String hashed = null;
        MessageDigest digest = null;
        try{
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(salted.getBytes(StandardCharsets.UTF_8));
            hashed = Base64.getEncoder().encodeToString(encodedHash);
        }
        catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }

        return hashed;
    }
}
