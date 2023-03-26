package sample.gui.tools;

import sample.gui.data.LoginData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class HashTool{

    private HashTool(){}

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

    public static int generateNewSalt(){
        List<LoginData.LoginDataItem> list = LoginData.getListInstance();
        Set<Integer> saltSet = list.stream().map(LoginData.LoginDataItem::salt).collect(Collectors.toSet());
        Random random = new Random();
        int salt = random.nextInt(Short.MAX_VALUE);

        while(saltSet.contains(salt))
            salt = random.nextInt();
        return salt;
    }
}
