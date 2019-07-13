package X;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

     @GetMapping("/register")
     public String register(){
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
