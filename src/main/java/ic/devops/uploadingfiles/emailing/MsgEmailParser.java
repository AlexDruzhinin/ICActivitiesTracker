package ic.devops.uploadingfiles.emailing;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import ic.devops.uploadingfiles.constants.ActivityTemplateConstants;
import ic.devops.uploadingfiles.exceptions.PEAEmailNotParsedException;
import ic.devops.uploadingfiles.model.EscalationActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class MsgEmailParser implements IEmailParser{

    @Autowired
    public MsgEmailParser() {}

    @Override
    public String getEmailBody(MultipartFile msgFile) {
        String ret;
        try {
            Message parsedMessage = new MsgParser().parseMsg(msgFile.getInputStream());
            String body = parsedMessage.getBodyText();
            Date date = parsedMessage.getDate();
            ret = date + body;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public Map parseBody(String body) throws PEAEmailNotParsedException {
        String[] lines = body.split("\n");
        Map<String, String> activityDetails = new HashMap();
        for (String line : lines) {
            if (!line.contains(":")) {
                continue;
            }
            String[] values = line.split(":", 2);
            activityDetails.put(values[0], values[1]);
        }
        if(activityDetails.isEmpty()) {
            throw new PEAEmailNotParsedException("Could not parse email! Incorrect lines format");
        }
        activityDetails.entrySet().stream().forEach((e)-> System.out.println(e));
        return activityDetails;
    }

    public EscalationActivity parseActivity(MultipartFile msgFile) throws PEAEmailNotParsedException {
        EscalationActivity activity = new EscalationActivity();
        Map<String, String> activityDetails = parseBody(getEmailBody(msgFile));
        activity.setType(activityDetails.get(ActivityTemplateConstants.TYPE));
        activity.setCustomer(activityDetails.get(ActivityTemplateConstants.CUSTOMER));
        activity.setPriority(activityDetails.get(ActivityTemplateConstants.PRIORITY));
        activity.setCreatedBy(activityDetails.get(ActivityTemplateConstants.CREATED_BY));


        return activity;
    }
}
