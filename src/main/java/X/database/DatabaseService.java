package X.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class DatabaseService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public abstract void insert(Object object);

    public abstract boolean exists(Object object);

    public boolean hasDuplicate(String table, String attribute, String value){
        String query = "SELECT * FROM "+table+" WHERE "+attribute+"='"+value+"'";
        if (jdbcTemplate.queryForList(query).isEmpty()) {
            return false;
        }
        return true;
    }



}