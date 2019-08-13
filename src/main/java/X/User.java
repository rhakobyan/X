package X;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class User {

    @NotNull
    @NotBlank(message="Username cannot be blank!")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,15}$", message = "Username is in the wrong format!")
    private String username;
    @NotNull
    @NotBlank(message="Email cannot be blank!")
    private String email;
    @NotNull
    @NotBlank(message="Password cannot be blank!")
    private String password;
    private String registration;

    private int reputation;

    private int ID;

    private String profilePictureLocation;

    private Role role;

    private ArrayList<Role> roles = new ArrayList<>();

    public User(){

    }
    public User(String username, String email, String password, String registration){
        this.username = username;
        this.email = email;
        this.password = password;
        this.registration = registration;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRegistration() {
        return registration;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getProfilePictureLocation() {
        return profilePictureLocation;
    }

    public void setProfilePictureLocation(String profilePictureLocation) {
        this.profilePictureLocation = "/files" + profilePictureLocation;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

   public void addRole(Role role){
        roles.add(role);
   }

   public ArrayList<Role> getRoles(){
        return roles;
   }

    public void generateFromMap(Map<String, Object> map){
        setUsername(map.get("username").toString());
        setRegistration(map.get("registration").toString());
        setEmail(map.get("email").toString());
        setReputation(Integer.parseInt(map.get("reputation").toString()));
        setID(Integer.parseInt(map.get("userID").toString()));
        setProfilePictureLocation(map.get("profilePictureLocation").toString());
    }
}
