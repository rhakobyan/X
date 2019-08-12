package X.database;

import X.NoSuchUserException;
import X.User;
import X.Role;
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
            String getIDQuery = "SELECT userID FROM User WHERE username='"+user.getUsername()+"'";
            Integer userID = jdbcTemplate.queryForObject(getIDQuery, Integer.class);
            String roleQuery = "INSERT INTO UserRole (userID, roleID) VALUES("+userID+",1)";
            jdbcTemplate.execute(roleQuery);
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
            String roleQuery = "SELECT * FROM Role INNER JOIN UserRole ON UserRole.roleID = Role.roleID WHERE userID = "+Integer.parseInt(users.get("userID").toString())+" ORDER BY priority DESC;";
            Map<String, Object> roleMap = jdbcTemplate.queryForList(roleQuery).get(0);
            Role role = new Role();
            role.generateFromMap(roleMap);
            user.generateFromMap(users);
            user.setRole(role);
            return user;
        }
        throw new NoSuchUserException("User does not exist");
    }

    public User findUserByLogin(String username, String password) throws NoSuchUserException {
        String query = "SELECT * FROM user WHERE username='"+username+"' AND password='"+password+"'";
        if (!jdbcTemplate.queryForList(query).isEmpty()) {
            User user = new User();
            Map<String, Object> users = jdbcTemplate.queryForList(query).get(0);
            String roleQuery = "SELECT * FROM Role INNER JOIN UserRole ON UserRole.roleID = Role.roleID WHERE userID = "+Integer.parseInt(users.get("userID").toString())+" ORDER BY priority DESC;";
            Map<String, Object> roleMap = jdbcTemplate.queryForList(roleQuery).get(0);
            Role role = new Role();
            role.generateFromMap(roleMap);
            user.generateFromMap(users);
            user.setRole(role);
            return user;
        }
        throw new NoSuchUserException("User does not exist");
    }

    public String getUsernameByID(int id){
        String query = "SELECT username FROM user WHERE userID="+id+"";
        String result =  jdbcTemplate.queryForObject(query, String.class);
        return result;
    }

    public void updateProfile(String filename, int userID){
        String query = "UPDATE "+TABLE_NAME+" SET profilePictureLocation='/"+filename+"' WHERE userID="+userID+"";
        jdbcTemplate.execute(query);
    }
}
