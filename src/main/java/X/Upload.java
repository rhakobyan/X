package X;

import java.util.ArrayList;
import java.util.Map;

public class Upload {
    private int uploadID;
    private String projectName;
    private String projectDescription;
    private String location;
    private String fileName;
    private String dateAdded;
    private int rating;
    private int uploaderID;
    private ArrayList<Tag> tagsList = new ArrayList<>();

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Upload(){}

    public Upload(String projectName, String projectDescription){
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setUploadID(int uploadID) {
        this.uploadID = uploadID;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setUploaderID(int uploaderID) {
        this.uploaderID = uploaderID;
    }

    public int getUploadID() {
        return uploadID;
    }

    public String getLocation() {
        return location;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public int getRating() {
        return rating;
    }

    public int getUploaderID() {
        return uploaderID;
    }

    public ArrayList<Tag> getTagsList() {
        return tagsList;
    }

    public void setTagsList(ArrayList<Tag> tags) {
        this.tagsList = tags;
    }

    public void addTag(Tag tag){
        tagsList.add(tag);
    }

    public void generateFromMap(Map<String, Object> map){
        setUploadID(Integer.parseInt(map.get("uploadID").toString()));
        setProjectName(map.get("projectName").toString());
        setProjectDescription(map.get("projectDescription").toString());
        setLocation(map.get("location").toString());
        setFileName(map.get("fileName").toString());
        setDateAdded(map.get("dateAdded").toString());
        setRating(Integer.parseInt(map.get("rating").toString()));
        setUploaderID(Integer.parseInt(map.get("uploaderID").toString()));
    }
}
