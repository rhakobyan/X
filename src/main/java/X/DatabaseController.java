package X;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class DatabaseController {


    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean hasDuplicate(String table, String attribute, String value){
        String query = "SELECT * FROM "+table+" WHERE "+attribute+"='"+value+"'";
        if (jdbcTemplate.queryForList(query).isEmpty()) {
            return false;
        }
        return true;
    }

    public void insertUser(User user){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String query = "INSERT INTO user (username, password, email, registration) VALUES ('"+user.getUsername()+"', '"+user.getPassword()+"', '"+user.getEmail()+"', '"+formatter.format(date)+"');";
        try {
            jdbcTemplate.execute(query);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean userExists(User user){
        String query = "SELECT username FROM user WHERE username='"+user.getUsername()+"' AND password='"+user.getPassword()+"';";
        return !jdbcTemplate.queryForList(query).isEmpty();

    }
}
