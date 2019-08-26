package X.database;

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
public class TagDatabaseService extends DatabaseService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String TABLE_NAME = "Tag";

    @Override
    public void insert(Object object) {
        if (!exists(object)) {
            Tag tag = (Tag) object;
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String query = "INSERT INTO Tag(name, description, dateAdded) VALUES('" + tag.getName() + "', '" + tag.getDescription() + "', '" + formatter.format(date) + "');";
            jdbcTemplate.execute(query);
        }
    }

    @Override
    public boolean exists(Object object) {
        Tag tag = (Tag) object;
        String query = "SELECT name FROM "+TABLE_NAME+" WHERE name='"+tag.getName()+"';";
        return !jdbcTemplate.queryForList(query).isEmpty();
    }

    public List<Tag> getAll(){
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY `usage` DESC;";
        List<Map<String, Object>> tagMap = jdbcTemplate.queryForList(query);
        return convertToTagList(tagMap);
    }

    public List<Tag> loadLimitedResults(int limit, int offset){
        String query = "SELECT * FROM " + TABLE_NAME +" ORDER BY `usage` DESC LIMIT " + limit+" OFFSET "+offset+";";
        return convertToTagList(jdbcTemplate.queryForList(query));
    }

    public List<String> loadLimitedAutocompleteResults(int limit, String patter){
        String query = "SELECT name FROM " + TABLE_NAME +" WHERE name LIKE '%"+patter+"%' ORDER BY `usage` DESC LIMIT " + limit+";";
        List<Map<String, Object>> tagsMap = jdbcTemplate.queryForList(query);
        ArrayList<String> tags = new ArrayList<>();
        for (int i =0; i<tagsMap.size(); i++){
            tags.add(tagsMap.get(i).get("name").toString());
        }
        return tags;
    }

    public int numberOfRecords(){
        return super.numberOfRecords(TABLE_NAME);
    }

    private List<Tag> convertToTagList(List<Map<String, Object>> tagsListMap){
        ArrayList<Tag> tags = new ArrayList<>();
        for (int i = 0; i < tagsListMap.size(); i++) {
            Tag tag = new Tag();
            tag.generateFromMap(tagsListMap.get(i));
            tags.add(tag);
        }
        return tags;
    }
}
