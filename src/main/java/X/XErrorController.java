package X;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class XErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response, Model model) {
        String errorMsg = "Error";
        int errorCode = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        switch (errorCode){
            case 400:
                errorMsg = "Bad Request!";
                break;
            case 401:
                errorMsg = "Unauthorised!";
                break;
            case 403:
                errorMsg = "Forbidden!";
                break;
            case 404:
                errorMsg = "Page Not Found!";
                break;
            case 500:
                errorMsg = "Internal Server Error!";
                break;
            case 502:
                errorMsg = "Bad Gateway!";
                break;
        }
        model.addAttribute("msg", errorCode + " " + errorMsg);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}


