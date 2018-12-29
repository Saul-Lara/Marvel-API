package DataBase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Connection {

    public String getapiKey(){
        String apiKey;
        String timestamp = "1";
        String publicKey = "";
        String privateKey = "";
        String hash;

        hash = getMD5(timestamp+privateKey+publicKey);
        apiKey = "ts="+ timestamp + "&apikey=" + publicKey + "&hash=" + hash;

        return apiKey;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
