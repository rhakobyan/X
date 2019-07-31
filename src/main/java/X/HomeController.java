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
    public String home() {
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
    public String userDetails(@PathVariable("username") String username, Model model){
        try {
            User user = userDatabaseService.findUserByUsername(username);
            System.out.println(user.getUsername());
            model.addAttribute("user", user);
            return "user";
        }
        catch (NoSuchUserException ex){
            System.out.println("This user does not exist!");
        }

        return "noUser";
    }

}
