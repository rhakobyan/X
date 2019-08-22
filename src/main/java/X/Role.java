package X;

import java.util.ArrayList;
import java.util.Map;

public class Role {

    private int id;

    private String name;

    private String colour;

    private String usernameColour;

    private ArrayList<String> permissions;

    private int priority;

    public Role(){

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

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getUsernameColour() {
        return usernameColour;
    }

    public void setUsernameColour(String usernameColour) {
        this.usernameColour = usernameColour;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void generateFromMap(Map<String, Object> map){
        setId(Integer.parseInt(map.get("roleID").toString()));
        setName(map.get("name").toString());
        setColour(map.get("colour").toString());
        setPriority(Integer.parseInt(map.get("priority").toString()));
    }

    public void setPermissions(ArrayList<String> permissions){
        this.permissions = permissions;
    }

    public ArrayList<String> getPermissions(){
        return permissions;
    }
}

