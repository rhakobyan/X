package X;

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
    HttpSession thisSession;

    @GetMapping("/cp")
    public String controlPanel(Model model){
        User user = sessionUser();
            if (user == null){
                model.addAttribute("user", null);
                return "redirect:/";
            }
            if (PermissionManager.hasControlPanelPermission(user)){
                List<Map<String, Object>> users = userDatabaseService.getAllUsers();
                System.out.println(users);
                model.addAttribute("users", users);
                model.addAttribute("user", user);
                return "cp";
            }
            return "redirect:/";


    }

    @GetMapping("/cp/tags")
    public String tags(Model model){
        model.addAttribute("tag", new Tag());
        User user = sessionUser();
        model.addAttribute("user", user);
        if (user == null){
            return "redirect:/";
        }
        else if (PermissionManager.hasControlPanelPermission(user)) {
            return "add-tag";
        }
        return "redirect:/";
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
