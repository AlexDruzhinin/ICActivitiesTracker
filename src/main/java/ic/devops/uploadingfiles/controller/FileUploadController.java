package ic.devops.uploadingfiles.controller;

import java.io.IOException;

import ic.devops.uploadingfiles.dao.ActivityRepository;
import ic.devops.uploadingfiles.emailing.MsgEmailParser;
import ic.devops.uploadingfiles.exceptions.PEAEmailNotParsedException;
import ic.devops.uploadingfiles.model.EscalationActivity;
import ic.devops.uploadingfiles.storage.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ic.devops.uploadingfiles.storage.StorageService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.GetMapping;

//@Slf4j
@Controller
//@RequestMapping("/")

public class FileUploadController {

    private final StorageService storageService;
    private final MsgEmailParser emailParser;
    private final ActivityRepository activityRepository;
    //private EscalationActivity activity;
    private boolean isDuplicate;

    @Autowired
    public FileUploadController(StorageService storageService, MsgEmailParser emailParser, ActivityRepository activityRepository) {
        this.storageService = storageService;
        this.emailParser = emailParser;
        this.activityRepository = activityRepository;
    }


    @GetMapping("/")
    public String showNewActivityForm(@ModelAttribute("activity") EscalationActivity activity, Model model) throws IOException {
        System.out.println("listUploadedFiles");
        if (!model.containsAttribute("message")) {
            System.out.println("message is empty");
        }
        if (activity == null) {
            activity = new EscalationActivity();
            System.out.println("activity was null, created!");
        }
        else {
            if (activity.getActivityID() != null) {
                System.out.println(activity.getActivityID());
            }
        }
        if (activity.getActivityID() != null) {
            model.addAttribute("activity", activity);
        }
        /*model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));*/

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                         RedirectAttributes redirectAttributes, Model model) throws PEAEmailNotParsedException {
        EscalationActivity activity;

        if (file != null) {
            activity = emailParser.parseActivity(file);
            if (activityRepository.findByActivityID(activity.getActivityID()) != null) {
                isDuplicate = true;
                System.out.println("duplicate");
                redirectAttributes.addFlashAttribute("duplicateActivity", "DuplicateMessgae");
            }
            //storageService.store(file);
            System.out.println("!!! upload !!!");
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
            redirectAttributes.addFlashAttribute("activity", activity);
            model.addAttribute("activity", activity);
        }
        return "redirect:/";


    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/pressAdd")
    public String pressAdd(@ModelAttribute("activity") EscalationActivity activity, Model model) {
        System.out.println("pressAdd()");
        return "redirect:/";
    }

    @PostMapping("/saveNew")
    public String saveNew(@ModelAttribute("activity") EscalationActivity activity) {
        //activityRepository.save(activity);
        System.out.println("saveNew()");
        return "activitiesList";
    }

    @GetMapping ("/toNew")
    public String toNew() {
        System.out.println("to new!!!");
        return "activitiesList";
    }


}