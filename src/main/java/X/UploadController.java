package X;

import X.database.UploadDatabaseService;
import X.database.UserDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class UploadController {

    @Autowired
    HttpSession thisSession;

    @Autowired
    UserDatabaseService userDatabaseService = new UserDatabaseService();
    @Autowired
    UploadDatabaseService uploadDatabaseService = new UploadDatabaseService();

    @GetMapping("/explore")
    public String firstExplorePage(HttpSession session, Model model) throws IOException {
        return listUploadedFiles(session, model, 1);
    }

    @GetMapping(path = "/explore", params="page")
    public String listUploadedFiles(HttpSession session, Model model, @RequestParam("page") int page) throws IOException {
        final int limit = 20;
       /*model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));*/
        if(session.getAttribute("user") != null)
        {
            int id = (int) session.getAttribute("user");
            User user = userDatabaseService.findUserByID(id);
            model.addAttribute("user", user);
        }
        int numberOfRecords = uploadDatabaseService.numberOfRecords();
        int numberOfPages = (int) Math.ceil((double)numberOfRecords / limit);
        model.addAttribute("pages", numberOfPages);
        List<Upload> uploads = uploadDatabaseService.loadLimitedResults(limit, limit*(page-1));
        for (int i=0; i<uploads.size(); i++){
            uploads.get(i).setUsername(userDatabaseService.getUsernameByID(uploads.get(i).getUploaderID()));
        }
        model.addAttribute("projects", uploads);
        //System.out.println();
        return "explore";
    }

    @GetMapping("/project/{projectName}")
    public String project(@PathVariable("projectName") String projectName, Model model){
        User user = sessionUser();
        model.addAttribute("user",user);
        Upload upload = uploadDatabaseService.load(projectName);
        model.addAttribute("project", upload);
        return "project";
    }

    private User sessionUser(){
        if(thisSession.getAttribute("user")== null){
            return null;
        }
        int id = (int) thisSession.getAttribute("user");
        User user = (User) userDatabaseService.findUserByID(id);
        return user;
    }

}
