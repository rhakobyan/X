package X;


import X.database.TagDatabaseService;
import X.database.UserDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    HttpSession thisSession;

    @Autowired
    UserDatabaseService userDatabaseService = new UserDatabaseService();
    @Autowired
    TagDatabaseService tagDatabaseService = new TagDatabaseService();

    @GetMapping("/")
    public String home(Model model) {
        if(sessionUser() != null){
            return "redirect:/explore";
        }
        model.addAttribute("user", sessionUser());
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("user", sessionUser());
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        model.addAttribute("user", sessionUser());
        return "contact";
    }


    @GetMapping("/user/{username}")
    public String userDetails(@PathVariable("username") String username, Model model, HttpSession session){
        try {
            User user = userDatabaseService.findUserByUsername(username);
            Integer id = (Integer) session.getAttribute("user");
            if (id != null) {
                User thisUser = (User) userDatabaseService.findUserByID(id);
                System.out.println(PermissionManager.hasChangeRoleManagementPermission(thisUser));
                boolean canChangeUsername = (PermissionManager.hasChangeUsernamePermission(thisUser) && (user.getRole().getPriority() >= thisUser.getRoles().get(0).getPriority()));
                model.addAttribute("changeUsername",canChangeUsername);
                model.addAttribute("roleManagement",PermissionManager.hasChangeRoleManagementPermission(thisUser));
                model.addAttribute("user", thisUser);
                ArrayList<String> roles = userDatabaseService.getAllRoles();
                ArrayList<String> userRoles = userDatabaseService.getUserRoles(user.getID());
                model.addAttribute("roles", roles);
                model.addAttribute("userRoles", userRoles);
            }
           else {
                model.addAttribute("changeUsername",false);
            }
            model.addAttribute("pageUser", user);
            return "user";
        }
        catch (NoSuchUserException ex){
            System.out.println("This user does not exist!");
        }

        return "noUser";
    }

    @GetMapping("/new-project")
    public String newProject(Model model, HttpSession session){
        if(session.getAttribute("user") == null)
        {
            model.addAttribute("user", null);
            return "redirect:/explore";
        }
        int id = (int) session.getAttribute("user");
        User user = userDatabaseService.findUserByID(id);
        model.addAttribute("upload", new Upload());
        model.addAttribute("user",user);
        return "upload";
    }

    @GetMapping("/tags")
    public String firstTagsPage(Model model) {
        return tags(model, 1);
    }

    @GetMapping(path = "/tags", params = "page")
    public String tags(Model model, @RequestParam("page") int page){
        int limit = 40;

        model.addAttribute("user", sessionUser());
        int numberOfRecords = tagDatabaseService.numberOfRecords();
        int numberOfPages = (int) Math.ceil((double)numberOfRecords / limit);
        model.addAttribute("pages", numberOfPages);
        List<Tag> tags= tagDatabaseService.loadLimitedResults(limit, limit*(page-1));
        model.addAttribute("tags", tags);
        return "tags";
    }

    @GetMapping("/autocompleteTags")
    @ResponseBody
    public String vzu(@RequestParam("value") String value){
        int limit = 5;
        List<String> tags= tagDatabaseService.loadLimitedAutocompleteResults(limit, value);
        return tags.toString();
    }

    @GetMapping("/log-out")
    public String logOut(){
        thisSession.invalidate();
        return "redirect:/";
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
