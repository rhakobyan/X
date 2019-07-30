package X.database;

import X.NoSuchUserException;
import X.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class UserDatabaseService extends DatabaseService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String TABLE_NAME = "User";

    public boolean hasDuplicate(String attribute, String value) {
       return super.hasDuplicate(TABLE_NAME, attribute, value);
    }

    public void insert(Object object) {
        User user = (User) object;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String query = "INSERT INTO "+TABLE_NAME+" (username, password, email, registration) VALUES ('"+user.getUsername()+"', '"+user.getPassword()+"', '"+user.getEmail()+"', '"+formatter.format(date)+"');";
        try {
            jdbcTemplate.execute(query);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean exists(Object object) {
        User user = (User) object;
        String query = "SELECT username FROM "+TABLE_NAME+" WHERE username='"+user.getUsername()+"' AND password='"+user.getPassword()+"';";
        return !jdbcTemplate.queryForList(query).isEmpty();
    }

    public User findUserByUsername(String username) throws NoSuchUserException {
        String query = "SELECT * FROM user WHERE username='"+username+"'";
        if (!jdbcTemplate.queryForList(query).isEmpty()) {
            User user = new User();
            Map<String, Object> users = jdbcTemplate.queryForList(query).get(0);
            user.setUsername(users.get("username").toString());
            user.setRegistration(users.get("registration").toString());
            user.setEmail(users.get("email").toString());
            user.setReputation(Integer.parseInt(users.get("reputation").toString()));
            user.setID(Integer.parseInt(users.get("userID").toString()));
            return user;
        }
        throw new NoSuchUserException("User does not exist");
    }
}
