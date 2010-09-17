
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encode {
    private static final String PREFIX = "Geomajas is a wonderful framework";
    private static final String PADDING = "==";

    public static void main(String[] args) {
        String login = args[0];
        String password = args[1];
        password = encode(PREFIX + login + password);
        if (password.endsWith(PADDING)) {
            password = password.substring(0, password.length() - 2);
        }
        System.out.println("login: " + login + ", password: " + password);
    }
    
    private static String encode(String plaintext) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plaintext.getBytes("UTF-8"));
            return Base64.encodeBytes(md.digest());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
