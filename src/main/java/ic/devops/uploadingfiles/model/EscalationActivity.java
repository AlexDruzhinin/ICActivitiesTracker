package ic.devops.uploadingfiles.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="escalations")
public class EscalationActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String srId;
    private String activityID;
    private String actTitle;
    private String srTitle;
    private String type;
    private String priority;
    private String createdBy;
    private String owner;
    private String customer;
    private Date creationDate;

    public EscalationActivity() {
        createdBy = "Sashka";
    }

    public String getSrId() {
        return srId;
    }

    public void setSrId(String srId) {
        this.srId = srId;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getPriority() { return priority;  }

    public void setPriority(String priority) { this.priority = priority; }

    public String getSrTitle() { return srTitle; }

    public void setSrTitle(String srTitle) { this.srTitle = srTitle; }

}
