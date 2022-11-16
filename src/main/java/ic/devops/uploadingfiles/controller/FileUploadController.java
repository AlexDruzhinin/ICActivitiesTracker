package ic.devops.uploadingfiles.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import ic.devops.uploadingfiles.emailing.MsgEmailParser;
import ic.devops.uploadingfiles.exceptions.PEAEmailNotParsedException;
import ic.devops.uploadingfiles.model.EscalationActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import ic.devops.uploadingfiles.storage.StorageService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/")

public class FileUploadController {

    private final StorageService storageService;
    private final MsgEmailParser emailParser;

    private EscalationActivity activity;

    @Autowired
    public FileUploadController(StorageService storageService, MsgEmailParser emailParser) {
        this.storageService = storageService;
        this.emailParser = emailParser;
    }

    @GetMapping
    public String main() {
        System.out.println("!!! main !!!");
        return "uploadForm";
    }

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file, /*@ModelAttribute("activity") EscalationActivity activity,*/ Model model,
                         RedirectAttributes redirectAttributes) throws PEAEmailNotParsedException {

        System.out.println("!!! upload !!!");
        if (file != null) {
            activity = emailParser.parseActivity(file);
        }
        //log.info("uploaded file " + file.getOriginalFilename() + "; \n" + emailParser.getEmailBody(file));
        //emailParser.parseBody(emailParser.getEmailBody(file));

        //model.addAttribute("message2", "The following activity will be created and added to DB");
        model.addAttribute("message", "Sashka");

        //model.addAttribute("values",  emailParser.parseBody(emailParser.getEmailBody(file)));

        return "uploadForm";
    }

    @PostMapping("/add")
    public String addUser(@Valid EscalationActivity activity, BindingResult result, Model model) {

        return "uploadForm";
    }
}