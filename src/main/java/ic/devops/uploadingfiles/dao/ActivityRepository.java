package ic.devops.uploadingfiles.dao;

import ic.devops.uploadingfiles.model.EscalationActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<EscalationActivity, Long> {
    public EscalationActivity findByActivityID(String activityID);

    public List<EscalationActivity> findAllBy();
}
