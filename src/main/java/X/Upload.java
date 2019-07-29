package X;

public class Upload {
    private int uploadID;
    private String projectName;
    private String projectDescription;
    private String location;
    private String fileName;
    private String dateAdded;
    private int rating;
    private int uploaderID;

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
}
