package ic.devops.uploadingfiles.emailing;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

public interface IEmailParser {
    String getEmailBody(MultipartFile emailFile) throws IOException;
    Date getEmailDate(MultipartFile emailFile) throws IOException;
}
