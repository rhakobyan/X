package X.database;

import X.NoSuchUserException;
import X.User;
import X.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
            String roleQuery = "SELECT * FROM Role INNER JOIN UserRole ON UserRole.roleID = Role.roleID WHERE userID = "+Integer.parseInt(users.get("userID").toString())+" ORDER BY priority;";
            Map<String, Object> roleMap = jdbcTemplate.queryForList(roleQuery).get(0);
            Role role = new Role();
            role.generateFromMap(roleMap);
            user.generateFromMap(users);
            user.setRole(role);
            return user;
        }
        throw new NoSuchUserException("User does not exist");
    }

    public User findUserByID(int id){
        String query = "SELECT * FROM user WHERE userID="+id+"";
        User user = new User();
        if (!jdbcTemplate.queryForList(query).isEmpty()) {
            Map<String, Object> users = jdbcTemplate.queryForList(query).get(0);
            user.generateFromMap(users);
            String roleQuery = "SELECT * FROM Role INNER JOIN UserRole ON UserRole.roleID = Role.roleID WHERE userID = "+Integer.parseInt(users.get("userID").toString())+" ORDER BY priority;";
            List<Map<String,Object>> roles = jdbcTemplate.queryForList(roleQuery);
            for (int i=0; i<roles.size(); i++){
                Role role = new Role();
                role.generateFromMap(roles.get(i));
                role.setPermissions(getPermissions(role.getId()));
                user.addRole(role);
            }
        }
        return user;
    }

    public User findUserByLogin(String username, String password) throws NoSuchUserException {
        String query = "SELECT * FROM user WHERE username='"+username+"' AND password='"+password+"'";
        if (!jdbcTemplate.queryForList(query).isEmpty()) {
            User user = new User();
            Map<String, Object> users = jdbcTemplate.queryForList(query).get(0);
            user.generateFromMap(users);
            String roleQuery = "SELECT * FROM Role INNER JOIN UserRole ON UserRole.roleID = Role.roleID WHERE userID = "+Integer.parseInt(users.get("userID").toString())+" ORDER BY priority;";
            List<Map<String,Object>> roles = jdbcTemplate.queryForList(roleQuery);
            for (int i=0; i<roles.size(); i++){
                Role role = new Role();
                role.generateFromMap(roles.get(i));
                role.setPermissions(getPermissions(role.getId()));
                user.addRole(role);
            }
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

    private ArrayList<String> getPermissions(int roleID){
        String query = "SELECT name FROM Permission INNER JOIN RolePermission ON Permission.permissionID = RolePermission.permissionID WHERE roleID="+roleID+"";
        List<Map<String,Object>> rolePermissions = jdbcTemplate.queryForList(query);
        ArrayList<String> permissions = new ArrayList<>();
        for (int i=0; i<rolePermissions.size(); i++){
            permissions.add(rolePermissions.get(i).get("name").toString());
        }
        return permissions;
    }

    public List<Map<String, Object>> getAllUsers(){
        String query = "SELECT * FROM User;";
        List<Map<String, Object>> users= jdbcTemplate.queryForList(query);
        return users;
    }

    public void changeUsername(String newUsername, String oldUsername){
            String query = "UPDATE "+TABLE_NAME+" SET username='"+newUsername+"' WHERE username='"+oldUsername+"'";
            jdbcTemplate.execute(query);


    }
}
