package sample.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_NAME_REGEX = Pattern.compile("^[a-zA-Z\\s]+", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile("(?:\\(\\d{3}\\)|\\d{3}[-]*)\\d{3}[-]*\\d{4}", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean validateName(String str) {
        Matcher matcher=VALID_NAME_REGEX.matcher(str);
        return matcher.find();
    }
    public static boolean validatePassword(String str) {
        Matcher matcher=VALID_PASSWORD_REGEX.matcher(str);
        return matcher.find();
    }
    public static boolean validatePhoneNumber(String str) {
        Matcher matcher=VALID_PHONE_NUMBER_REGEX.matcher(str);
        return matcher.find();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static File blobToFile(Blob blob, String filename) {
        File file = null;
        try {
            byte[] array = blob.getBytes(1, (int) blob.length());
            file = File.createTempFile(filename, ".binary", new File("temp/"));
            FileOutputStream out = new FileOutputStream(file);
            out.write(array);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
