package X;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {

    @Autowired
    JdbcTemplate jdbcTemplate;;

    @GetMapping("/")
    public String home() {
        return "index";
    }

     @GetMapping("/register")
     public String register(){

        return "register";
     }

     @PostMapping("/register")
     public String registration(@RequestParam(value = "username") String username, @RequestParam(value = "email") String email,
                                @RequestParam(value = "password") String password){
         Date date = new Date();
         SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
         System.out.println("The username is " + username);
       System.out.println("The email is " + email);
       password = Validation.hash(password);
       System.out.println("The password is " + password);
         System.out.println("The date is " + formatter.format(date));
         String query = "INSERT INTO user (username, password, email, registration) VALUES ('"+username+"', '"+password+"', '"+email+"', '"+formatter.format(date)+"');";
         jdbcTemplate.execute(query);
        return "register";
     }
    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/explore")
    public String explore(){
        return "explore";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

}
