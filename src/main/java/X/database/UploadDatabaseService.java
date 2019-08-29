package X.database;

import X.Tag;
import X.Upload;
import X.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class UploadDatabaseService extends DatabaseService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    TagDatabaseService tagDatabaseService = new TagDatabaseService();
    @Autowired
    UserDatabaseService userDatabaseService  = new UserDatabaseService();

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
        String getUploadID = "SELECT uploadID from Upload WHERE projectName='"+upload.getProjectName()+"'";
        Integer id = jdbcTemplate.queryForObject(getUploadID, Integer.class);
        for (int i=0; i<upload.getTagsList().size(); i++) {
            String addTag = "INSERT INTO UploadTag(uploadID, tagID) VALUES("+id+ "," + upload.getTagsList().get(i).getId() + ")";
            jdbcTemplate.execute(addTag);
        }
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

    public List<Upload> loadLimitedResults(int limit, int offset){
        String query = "SELECT * FROM " + TABLE_NAME +" ORDER BY rating DESC LIMIT " + limit+" OFFSET "+offset+";";
        return convertToUploadList(jdbcTemplate.queryForList(query));
    }

    public int numberOfRecords(){
        return super.numberOfRecords(TABLE_NAME);
    }

    public Upload load(String projectName){
        String query = "SELECT * FROM " + TABLE_NAME +" WHERE projectName = '"+projectName+"';";
        Map<String, Object> uploadMap= jdbcTemplate.queryForMap(query);
        Upload upload = new Upload();
        upload.generateFromMap(uploadMap);
        String tagQuery = "SELECT tagID FROM UploadTag WHERE uploadID = "+upload.getUploadID()+";";
        List<Map<String, Object>> uploadTagList = jdbcTemplate.queryForList(tagQuery);
        for (int j =0; j<uploadTagList.size(); j++){
            Tag tag = tagDatabaseService.get(Integer.parseInt(uploadTagList.get(j).get("tagID").toString()));
            upload.addTag(tag);
        }

        User user = userDatabaseService.findUserByID(upload.getUploaderID());
        upload.setUser(user);
        return upload;
    }

    public void vote(String projectName, int userID, boolean positive) throws Exception{
        String getUploadId = "SELECT uploadID FROM "+TABLE_NAME+" WHERE projectName = '"+projectName+"'";
        Integer id = jdbcTemplate.queryForObject(getUploadId, Integer.class);
        System.out.println("HERE");
            String votedQuery = "SELECT positive FROM VotesCast WHERE userID="+userID+" AND uploadID="+id+"";
            if (jdbcTemplate.queryForList(votedQuery).isEmpty()) {
                String voteQuery = "INSERT INTO VotesCast(uploadID, userID, positive) VALUES(" + id + "," + userID + "," + positive + ")";
                jdbcTemplate.execute(voteQuery);
            }
            else {
                Boolean voteType = jdbcTemplate.queryForObject(votedQuery, Boolean.class);
                if(voteType != null) {
                    if ((voteType && !positive) || (!voteType && positive)) {
                        String updateQuery = "UPDATE VotesCast SET positive=" + positive + " WHERE uploadID=" + id + " AND userID=" + userID + ";";
                        jdbcTemplate.execute(updateQuery);
                    } else {
                        throw new Exception("Already voted");
                    }
                }
            }
            int n = positive ? 1:-1;
            String voteCount = "UPDATE Upload SET rating = rating+"+n+" WHERE uploadID = "+id+"";
            jdbcTemplate.execute(voteCount);
    }
    private List<Upload> convertToUploadList(List<Map<String, Object>> uploadsListMap){
        if (!uploadsListMap.isEmpty()) {
            List<Upload> uploadList= new ArrayList<>();
           for (int i=0; i<uploadsListMap.size(); i++){
               Map<String, Object> uploadMap = uploadsListMap.get(i);
               Upload upload = new Upload();
               upload.generateFromMap(uploadMap);
               String query = "SELECT tagID FROM UploadTag WHERE uploadID = "+upload.getUploadID()+";";
               List<Map<String, Object>> uploadTagList = jdbcTemplate.queryForList(query);
               for (int j =0; j<uploadTagList.size(); j++){
                   Tag tag = tagDatabaseService.get(Integer.parseInt(uploadTagList.get(j).get("tagID").toString()));
                   upload.addTag(tag);
               }
               uploadList.add(upload);
           }
           return uploadList;
        }
        return null;
    }

}
