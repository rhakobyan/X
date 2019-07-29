package X;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AuthenticationController {

    @Autowired
    DatabaseController homeDatabaseController = new DatabaseController();

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registration(@Valid User user, BindingResult result){
        user.setPassword(Validation.hash(user.getPassword()));
        if(!Validation.emailValidation(user.getEmail())){
            result.rejectValue("email", "error.user", "Email is in the wrong format!");
        }

        if(homeDatabaseController.hasDuplicate("user", "email", user.getEmail())){
            result.rejectValue("email", "error.user", "Email is already taken!");
        }
        if(homeDatabaseController.hasDuplicate("user", "username", user.getUsername())){
            result.rejectValue("username", "username.user", "Username is already taken!");
        }
        if(result.hasErrors()){
            return "register";
        }
        homeDatabaseController.insertUser(user);
        return "redirect:/explore";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String logging(User user, BindingResult result, HttpSession session){
        user.setPassword(Validation.hash(user.getPassword()));
            try {
                user = homeDatabaseController.findUserByUsername(user.getUsername());
                session.setAttribute("user", user);
                return "redirect:/explore";
            }
           catch (NoSuchUserException ex){
               result.rejectValue("username", "username.user", "The username or password is wrong!");
               return "login";
           }


    }
}
