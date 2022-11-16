package ic.devops.uploadingfiles.validation;

import ic.devops.uploadingfiles.model.EscalationActivity;

public interface IValidator {
    public boolean validateActivity (EscalationActivity activity);
}
