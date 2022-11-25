package ic.devops.uploadingfiles.controller;

import ic.devops.uploadingfiles.dao.ActivityRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/activities")
public class ActivityController {

    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    ActivityRepository activityRepository;

    @GetMapping("/{act_id}")
    public String showActivity(@PathVariable("act_id") String act_id, Model model) {
        model.addAttribute("activity", activityRepository.findByActivityID(act_id));
        return "activityForm";
    }

    @GetMapping("/")
    public String showAll(Model model) {
        model.addAttribute("activities", activityRepository.findAll());
        return "activitiesList";
    }
}
