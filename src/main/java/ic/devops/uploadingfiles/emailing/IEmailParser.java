package ic.devops.uploadingfiles.emailing;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IEmailParser {
    String getEmailBody(MultipartFile emailFile) throws IOException;
}
