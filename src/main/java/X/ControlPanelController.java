package X;

import X.database.PermissionDatabaseService;
import X.database.RoleDatabaseService;
import X.database.TagDatabaseService;
import X.database.UserDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ControlPanelController {

    @Autowired
    UserDatabaseService userDatabaseService = new UserDatabaseService();
    @Autowired
    TagDatabaseService tagDatabaseService = new TagDatabaseService();
    @Autowired
    RoleDatabaseService roleDatabaseService = new RoleDatabaseService();
    @Autowired
    PermissionDatabaseService permissionDatabaseService = new PermissionDatabaseService();
    @Autowired
    HttpSession thisSession;

    @GetMapping("/cp")
    public String redirectCp(){
        return "redirect:/cp/main";
    }
    @GetMapping("/cp/{panel}")
    public String controlPanel(@PathVariable String panel, Model model){
        String template = "";
        User user = sessionUser();
            if (user == null){
                model.addAttribute("user", null);
                return "redirect:/";
            }
            if (PermissionManager.hasControlPanelPermission(user)){
                if (panel.equals("main")) {
                    List<Map<String, Object>> users = userDatabaseService.getAllUsers();
                    System.out.println(users);
                    model.addAttribute("users", users);
                    template = "cp";
                }
               else if(panel.equals("tags")){
                    template = tags(model, user);
                }
               else if(panel.equals("roles")){
                   template = roles(model, user);
                }
                model.addAttribute("user", user);

               return template;
            }
            return "redirect:/";


    }
    private String tags(Model model, User user){
        model.addAttribute("tag", new Tag());

        if (PermissionManager.hasControlPanelPermission(user)) {
            return "add-tag";
        }
        return "redirect:/";
    }

    private String roles(Model model, User user){
           List<Role> roles = roleDatabaseService.loadAll();
           List<Permission> permissions = permissionDatabaseService.loadAll();
           model.addAttribute("permissions", permissions);
           model.addAttribute("roles",roles);
            return "roles";

    }

    @PostMapping("/cp/tags")
    public String submitTag(Tag tag){
        tagDatabaseService.insert(tag);
        return "redirect:/cp/tags";
    }

    private User sessionUser(){
        if(thisSession.getAttribute("user")== null){
            return null;
        }
        int id = (int) thisSession.getAttribute("user");
        User user = (User) userDatabaseService.findUserByID(id);
        return user;
    }

}
