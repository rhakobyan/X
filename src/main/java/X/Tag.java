package X;

import java.util.Map;

public class Tag {

    private int id;

    private String name;

    private String description;

    private int usage;

    private String dateAdded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void generateFromMap(Map<String, Object> map){
        name = map.get("name").toString();
        description = map.get("description").toString();
        id = Integer.parseInt(map.get("tagID").toString());
        dateAdded = map.get("dateAdded").toString();
        usage = Integer.parseInt(map.get("usage").toString());
    }
}
