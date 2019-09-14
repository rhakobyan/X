package X.database;

import X.Permission;
import X.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class PermissionDatabaseService extends DatabaseService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String TABLE_NAME = "Permission";

    public void insert(Object object) {
        if (!exists(object)) {
            Permission permission = (Permission) object;
            String query = "INSERT INTO Permissoin(name, description) VALUES('" + permission.getName() + "', '" + permission.getDescription() + "');";
            jdbcTemplate.execute(query);
        }
    }

    public boolean exists(Object object) {
        Role role = (Role) object;
        String query = "SELECT name FROM "+TABLE_NAME+" WHERE name='"+role.getName()+"';";
        return !jdbcTemplate.queryForList(query).isEmpty();
    }

    public List<Permission> loadAll(){
        String query = "SELECT * FROM " + TABLE_NAME +";";
        return convertToPermissionList(jdbcTemplate.queryForList(query));
    }

    private List<Permission> convertToPermissionList(List<Map<String, Object>> permissionsListMap){
        ArrayList<Permission> permissions = new ArrayList<>();
        for (int i = 0; i < permissionsListMap.size(); i++) {
            Permission permission = new Permission();
            permission.generateFromMap(permissionsListMap.get(i));
            permissions.add(permission);
        }
        return permissions;
    }
}

