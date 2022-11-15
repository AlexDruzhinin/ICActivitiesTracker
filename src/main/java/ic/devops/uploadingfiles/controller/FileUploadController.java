package ic.devops.uploadingfiles.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import ic.devops.uploadingfiles.emailing.MsgEmailParser;
import ic.devops.uploadingfiles.exceptions.PEAEmailNotParsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import ic.devops.uploadingfiles.storage.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")

public class FileUploadController {

    private final StorageService storageService;
    private final MsgEmailParser emailParser;

    String message2 = "The following activity will be created and added to DB";

    @Autowired
    public FileUploadController(StorageService storageService, MsgEmailParser emailParser) {
        this.storageService = storageService;
        this.emailParser = emailParser;
    }



    @PostMapping
    public void upload(@RequestParam("file") MultipartFile file, Model model) throws PEAEmailNotParsedException {

        log.info("uploaded file " + file.getOriginalFilename() + "; \n" + emailParser.getEmailBody(file));
        emailParser.parseBody(emailParser.getEmailBody(file));

        model.addAttribute("message2", "The following activity will be created and added to DB");
        model.addAttribute("values",  emailParser.parseBody(emailParser.getEmailBody(file)));


    }

    //@GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    public String fileParser(MultipartFile file) {

        return "";
    }

    /*
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public void upload(@RequestParam("file") MultipartFile file) {
        System.out.println("uploaded file " + file.getOriginalFilename());
    }

    /*@GetMapping("/")
    public String returnIndex()
    {
        return "index";
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
*/
}