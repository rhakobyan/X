package X.database;

import X.NoSuchUserException;
import X.User;
import X.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public ArrayList<String> getAllRoles(){
        String query = "SELECT name FROM Role;";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        ArrayList<String> roles = new ArrayList<>();
        for (int i=0; i<result.size(); i++){
            roles.add(result.get(i).get("name").toString());
        }

        return roles;
    }

    public ArrayList<String> getUserRoles(int userid){
        String query1= "SELECT roleID FROM UserRole WHERE userID = "+userid+"";
        String query = "SELECT name FROM Role WHERE roleID IN ("+query1+");";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        ArrayList<String> roles = new ArrayList<>();
        for (int i=0; i<result.size(); i++){
            roles.add(result.get(i).get("name").toString());
        }

        return roles;
    }

    public void addRoleToUser(String username, String roleName){
        int roleid = findRoleIDByName(roleName);
        int userid = findUserIDByName(username);
        String query = "INSERT INTO UserRole (roleID, userID) VALUES ("+roleid+", "+userid+")";
        jdbcTemplate.execute(query);
    }

    public void removeRoleFromUser(String username, String roleName){
        int roleid = findRoleIDByName(roleName);
        int userid = findUserIDByName(username);
        String query = "DELETE FROM UserRole WHERE roleID = "+roleid+" AND userID= "+userid+";";
        jdbcTemplate.execute(query);
    }

    public void updateReputation(int userID, int amount){
        String query = "UPDATE User SET reputation = reputation+"+amount+" WHERE userID = "+userID+";";
        jdbcTemplate.execute(query);
    }

    private int findRoleIDByName(String name){
        String query = "SELECT roleID FROM Role WHERE name = '"+name+"'";
        Integer id = jdbcTemplate.queryForObject(query, Integer.class);
        return id;
    }

    private int findUserIDByName(String username){
        String query = "SELECT userID FROM User WHERE username = '"+username+"'";
        Integer id = jdbcTemplate.queryForObject(query, Integer.class);
        return id;
    }
}
