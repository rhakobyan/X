package X;

import java.util.ArrayList;
import java.util.Map;

public class Permission {

    private int id;

    private String name;

    private String description;

    public Permission(){

    }

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

    public void generateFromMap(Map<String, Object> map){
        setId(Integer.parseInt(map.get("permissionID").toString()));
        setName(map.get("name").toString());
        setDescription(map.get("description").toString());
    }
}

