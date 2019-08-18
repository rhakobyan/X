package X;


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
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {

    User thisUser;

    @Autowired
    UserDatabaseService userDatabaseService = new UserDatabaseService();

    @GetMapping("/")
    public String home(HttpServletRequest request) {
            System.out.println(request.getHeader("user-agent"));

        return "index";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }


    @GetMapping("/user/{username}")
    public String userDetails(@PathVariable("username") String username, Model model, HttpSession session){
        try {
            User user = userDatabaseService.findUserByUsername(username);
            Integer id = (Integer) session.getAttribute("user");
            if (id != null) {
                User thisUser = (User) userDatabaseService.findUserByID(id);
                System.out.println(PermissionManager.hasChangeUsernamePermission(thisUser));
                model.addAttribute("changeUsername",PermissionManager.hasChangeUsernamePermission(thisUser));
                model.addAttribute("roleManagement",PermissionManager.hasChangeRoleManagementPermission(thisUser));
                model.addAttribute("signedUser", thisUser);
            }
           else {
                model.addAttribute("changeUsername",false);
            }
            model.addAttribute("user", user);
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

}
