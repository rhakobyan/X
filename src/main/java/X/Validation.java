package X;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.regex.Pattern;


public class Validation { //class declaration

    @Autowired
    private static JdbcTemplate template;

    public static boolean hasErrors(User user){
        if(emailValidation(user.getEmail())) {
            return true;
        }
        else if(emailUsed(user.getEmail())){
            return true;
        }

        return false;
    }

    public boolean passwordsMatch(String firstPassword, String secondPassword) //method for checking if two entered
    //passwords match
    {
        if (firstPassword.equals(secondPassword)) {     //if the string value of the first password fields
            return true;                                //equal the second one, return true, that is passwords match
        }
        return false;                                   //passwords do not match, hence return false
    }

    public static boolean emailUsed(String email){
        String query = "SELECT email FROM user WHERE email='"+email+"'";
        if(template.queryForList(query) != null){
            return true;
        }
        return false;
    }

    public static boolean emailValidation(String email) //method for checking if the email is in the right format
    {
        if(Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches())
        //check using pattern class if the entered email follows the pattern
        {
            return true;    //if yes, return true, that is mail is in the right format
        }

        return false;   //not in the right format, so return false
    }

    public static String hash(String passwordToHash) //ecrypt the password
    {
        String generatedPassword = null;    //declare the generatedpassword variale
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) //catch exeptions
        {
            e.printStackTrace(); // prints the stack trace of the Exception
        }
        return(generatedPassword); //return the password to the program
    }

}
