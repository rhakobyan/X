package X;

import X.database.DatabaseService;
import X.database.UserDatabaseService;
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
    UserDatabaseService userDatabaseService = new UserDatabaseService();

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

        if(userDatabaseService.hasDuplicate("email", user.getEmail())){
            result.rejectValue("email", "error.user", "Email is already taken!");
        }
        if(userDatabaseService.hasDuplicate("username", user.getUsername())){
            result.rejectValue("username", "username.user", "Username is already taken!");
        }
        if(result.hasErrors()){
            return "register";
        }
        userDatabaseService.insert(user);
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
                user = userDatabaseService.findUserByLogin(user.getUsername(), user.getPassword());
                if (PermissionManager.hasLogInPermission(user)) {
                    session.setAttribute("user", user.getID());
                    return "redirect:/explore";
                }
                else {
                    result.rejectValue("username", "username.user", "You are not allowed to log in!");
                    return "login";
                }
            }
           catch (NoSuchUserException ex){
               result.rejectValue("username", "username.user", "The username or password is wrong!");
               return "login";
           }


    }
}
