package ic.devops.uploadingfiles.validation;

import ic.devops.uploadingfiles.model.EscalationActivity;

public class Validator implements IValidator{
    @Override
    public boolean validateActivity(EscalationActivity activity) {
        return false;
    }
}
