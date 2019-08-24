package X.database;

import X.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class UploadDatabaseService extends DatabaseService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String TABLE_NAME = "Upload";

    public boolean hasDuplicate(String attribute, String value) {
        return super.hasDuplicate(TABLE_NAME, attribute, value);
    }

    public void insert(Object object) {
        Upload upload = (Upload) object;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        int uploaderID = 0;
        String query = "INSERT INTO Upload (projectName, projectDescription, location, filename, dateAdded, uploaderID) VALUES" +
                "('"+upload.getProjectName()+"', '"+upload.getProjectDescription()+"', 'upload-dir/projects', '"+upload.getFileName()+"'" +
                ", '"+formatter.format(date)+"', '"+upload.getUploaderID()+"')";
        jdbcTemplate.execute(query);
    }

    public boolean exists(Object object) {
        Upload upload = (Upload) object;
        String query = "SELECT username FROM "+TABLE_NAME+" WHERE projectName='"+upload.getProjectName()+"'";
        return !jdbcTemplate.queryForList(query).isEmpty();
    }

    public List<Upload> loadAll(){
        String query = "SELECT * FROM " + TABLE_NAME +" ORDER BY rating DESC";
        return convertToUploadList(jdbcTemplate.queryForList(query));
    }

    public List<Upload> loadLimitedResults(int limit){
        String query = "SELECT * FROM " + TABLE_NAME +" ORDER BY rating DESC LIMIT " + limit+";";
        return convertToUploadList(jdbcTemplate.queryForList(query));
    }

    public int numberOfRecords(){
        String query = "SELECT COUNT(*) FROM "+TABLE_NAME+";";
        Integer records = jdbcTemplate.queryForObject(query, Integer.class);
        if (records != null) {
            return records;
        }
        return 0;
    }

    private List<Upload> convertToUploadList(List<Map<String, Object>> uploadsListMap){
        if (!uploadsListMap.isEmpty()) {
            List<Upload> uploadList= new ArrayList<>();
           for (int i=0; i<uploadsListMap.size(); i++){
               Map<String, Object> uploadMap = uploadsListMap.get(i);
               Upload upload = new Upload();
               upload.generateFromMap(uploadMap);
               uploadList.add(upload);
           }
           return uploadList;
        }
        return null;
    }

}
