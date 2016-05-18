package com.sos.dailyschedule.job;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;

@JSOptionClass(name = "CheckDailyScheduleOptions", description = "Checking a DailySchedule with runs in History")
public class CheckDailyScheduleOptions extends CheckDailyScheduleOptionsSuperClass {

    private static final long serialVersionUID = 1L;

    public CheckDailyScheduleOptions() {
    }

    public CheckDailyScheduleOptions(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    }

    public CheckDailyScheduleOptions(HashMap<String, String> JSSettings) throws Exception {
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