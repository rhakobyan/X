package X.database;

import X.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

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

}
