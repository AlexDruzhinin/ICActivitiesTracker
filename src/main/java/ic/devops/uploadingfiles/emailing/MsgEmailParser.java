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

    @Override
    public Date getEmailDate(MultipartFile msgFile) {
        Date date;
        try {
            Message parsedMessage = new MsgParser().parseMsg(msgFile.getInputStream());
            date = parsedMessage.getDate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return date;
    }


    public Map parseBody(String body) throws PEAEmailNotParsedException {
        String[] lines = body.split("\n");
        Map<String, String> activityDetails = new HashMap();
        for (String line : lines) {
            if (line.contains("You have been assigned Activity# ")) {
                //the line is usual like: You have been assigned Activity# 1-8VOHY46 for SR# 1-19319707302
                activityDetails.put(ActivityTemplateConstants.ACT_ID, line.substring(line.indexOf("#")+2, line.indexOf("#")+11));
                activityDetails.put(ActivityTemplateConstants.SR_ID, line.substring(line.lastIndexOf("#")+2, line.length()-1));
                continue;
            }
            if (!line.contains(":")) {
                continue;
            }
            String[] values = line.split(":", 2);
            activityDetails.put(values[0], values[1].trim());
        }
        if(activityDetails.isEmpty()) {
            throw new PEAEmailNotParsedException("Could not parse email! Incorrect lines format");
        }
        //activityDetails.entrySet().stream().forEach((e)-> System.out.println(e));
        return activityDetails;
    }

    public EscalationActivity parseActivity(MultipartFile msgFile) throws PEAEmailNotParsedException {
        EscalationActivity activity = new EscalationActivity();
        Map<String, String> activityDetails = parseBody(getEmailBody(msgFile));

        activity.setActTitle(activityDetails.get(ActivityTemplateConstants.ACTIVITY_TITLE));
        activity.setSrTitle(activityDetails.get(ActivityTemplateConstants.SR_TITLE));
        activity.setType(activityDetails.get(ActivityTemplateConstants.TYPE));
        activity.setCustomer(activityDetails.get(ActivityTemplateConstants.CUSTOMER));
        activity.setPriority(activityDetails.get(ActivityTemplateConstants.PRIORITY));
        activity.setCreatedBy(activityDetails.get(ActivityTemplateConstants.CREATED_BY));
        activity.setActivityID(activityDetails.get(ActivityTemplateConstants.ACT_ID));
        activity.setSrId(activityDetails.get(ActivityTemplateConstants.SR_ID));
        activity.setCreationDate(getEmailDate(msgFile));

        return activity;
    }
}
