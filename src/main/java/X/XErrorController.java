package X;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class XErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model) {
        String errorMsg = "";
        int httpErrorCode = HttpStatus.NOT_FOUND.value();

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "400 Bad Request";
                break;
            }
            case 401: {
                errorMsg = "401 Unauthorized";
                break;
            }
            case 404: {
                errorMsg = "404 Page not found";
                break;
            }
            case 500: {
                errorMsg = "500 Internal Server Error";
                break;
            }
        }
        model.addAttribute("msg", errorMsg);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}


