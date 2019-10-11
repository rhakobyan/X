package X.database;

import X.Role;
import X.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class RoleDatabaseService extends DatabaseService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String TABLE_NAME = "Role";

    public void insert(Object object) {
        if (!exists(object)) {
            Role role = (Role) object;
            String query = "INSERT INTO Role(name, colour, priority) VALUES('" + role.getName() + "', '" + role.getColour() + "', '" + role.getPriority()+ "');";
            jdbcTemplate.execute(query);
        }
    }

    public boolean exists(Object object) {
        Role role = (Role) object;
        String query = "SELECT name FROM "+TABLE_NAME+" WHERE name='"+role.getName()+"';";
        return !jdbcTemplate.queryForList(query).isEmpty();
    }

    public List<Role> loadAll(){
        String query = "SELECT * FROM " + TABLE_NAME +" ORDER BY priority;";
        return convertToRoleList(jdbcTemplate.queryForList(query));
    }

    public Role get(String roleName){
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE name = '"+roleName+"';";
        Map<String, Object> queryMap = jdbcTemplate.queryForMap(query);
        Role role = new Role();
        role.generateFromMap(queryMap);
        return role;
    }

    private List<Role> convertToRoleList(List<Map<String, Object>> rolesListMap){
        ArrayList<Role> roles = new ArrayList<>();
        for (int i = 0; i < rolesListMap.size(); i++) {
            Role role = new Role();
            role.generateFromMap(rolesListMap.get(i));
            roles.add(role);
        }
        return roles;
    }
}
