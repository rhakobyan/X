package X;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    DatabaseController homeDatabaseController = new DatabaseController();

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/explore")
    public String explore(HttpSession session){
        //This prints different results after closing the browser
        System.out.println(session.getId());
        if(session.getAttribute("user") != null) {
            thisUser = (User) session.getAttribute("user");
        }
        return "explore";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }


    @GetMapping("/user/{username}")
    public String userDetails(@PathVariable("username") String username, Model model){
        try {
            User user = homeDatabaseController.findUserByUsername(username);
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
