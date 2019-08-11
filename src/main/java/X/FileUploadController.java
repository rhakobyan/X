package X;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import X.database.UploadDatabaseService;
import X.database.UserDatabaseService;
import X.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import X.storage.StorageFileNotFoundException;
import X.storage.StorageService;

import javax.servlet.http.HttpSession;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    @Autowired
    private UploadDatabaseService uploadDatabaseService = new UploadDatabaseService();
    @Autowired
    private UserDatabaseService userDatabaseService = new UserDatabaseService();

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/explore")
    public String listUploadedFiles(Model model) throws IOException {

       /*model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));*/
        List<Upload> uploads = uploadDatabaseService.loadAll();
        for (int i=0; i<uploads.size(); i++){
            uploads.get(i).setUsername(userDatabaseService.getUsernameByID(uploads.get(i).getUploaderID()));
        }
           model.addAttribute("projects", uploads);
           //model.addAttribute("projectUserName", uploadDatabaseService.getUserNameByID())

        return "explore";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/new-project")
    public String handleFileUpload(Upload upload, @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, HttpSession session) {
        User user = (User) session.getAttribute("user");
        upload.setUploaderID(user.getID());
        try {
            storageService.store(file, upload.getProjectName()+"-"+user.getID());
            upload.setFileName(upload.getProjectName()+"-"+user.getID()+"-"+file.getOriginalFilename());
            uploadDatabaseService.insert(upload);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + upload.getFileName() + "!");
            return "redirect:/explore";
        }
        catch (StorageException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/explore";
        }

    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{username}")
    public String usedUpdate(@RequestParam("file") MultipartFile file, HttpSession session){
        User user = (User) session.getAttribute("user");
        String filename = user.getUsername()+"-"+user.getID();
        storageService.store(file, user.getUsername()+"-"+user.getID());
        filename= filename+"-"+file.getOriginalFilename();
       filename = filename.replaceAll("\\s+","-");
        userDatabaseService.updateProfile(filename, user.getID());
        return "redirect:/";
    }

}

