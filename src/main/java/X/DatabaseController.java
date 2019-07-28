package X;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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


    public User findUserByUsername(String username) throws NoSuchUserException{
        String query = "SELECT * FROM user WHERE username='"+username+"'";
        if (!jdbcTemplate.queryForList(query).isEmpty()) {
            User user = new User();
            Map<String, Object> users = jdbcTemplate.queryForList(query).get(0);
            user.setUsername(users.get("username").toString());
            user.setRegistration(users.get("registration").toString());
            user.setEmail(users.get("email").toString());
            user.setReputation(Integer.parseInt(users.get("reputation").toString()));
            return user;
        }
        throw new NoSuchUserException("User does not exist");
    }
}
