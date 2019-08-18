package X;

import X.database.UserDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ControlPanelController {

    @Autowired
    UserDatabaseService userDatabaseService = new UserDatabaseService();

    @GetMapping("/cp")
    public String controlPanel(HttpSession session, Model model){
        int id = (int) session.getAttribute("user");
        User user = userDatabaseService.findUserByID(id);
            if (user == null){
                return "redirect:/";
            }
            if (PermissionManager.hasControlPanelPermission(user)){
                List<Map<String, Object>> users = userDatabaseService.getAllUsers();
                System.out.println(users);
                model.addAttribute("users", users);
                return "cp";
            }
            return "redirect:/";


    }

}
