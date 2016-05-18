package com.sos.dailyschedule.job;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;

@JSOptionClass(name = "CreateDaysScheduleOptions", description = "Creating a DaysSchedule depending on actual Runtimes")
public class CreateDailyScheduleOptions extends CreateDailyScheduleOptionsSuperClass {

    private static final long serialVersionUID = 1L;

    public CreateDailyScheduleOptions() {
    }

    public CreateDailyScheduleOptions(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    }

    public CreateDailyScheduleOptions(HashMap<String, String> JSSettings) throws Exception {
        super(JSSettings);
    }

    @Override
    public void checkMandatory() {
        try {
            super.checkMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    }

}