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
    @Autowired
    HttpSession session;

    User thisUser;
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/explore")
    public String explore(Model model){
        System.out.println("^^^^^^^^^^^^" + session.getId());
        if(session.getAttribute("user") != null) {
            thisUser = (User) session.getAttribute("user");
            model.addAttribute("anonymous", false);

            model.addAttribute("user", thisUser);
            System.out.println("The username is "+ thisUser.getUsername());
        }
        else {
            model.addAttribute("anonymous", true);
        }
        return "explore";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

}
