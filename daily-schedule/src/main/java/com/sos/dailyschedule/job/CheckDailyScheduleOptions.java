package com.sos.dailyschedule.job;

import java.util.HashMap;
import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import org.apache.log4j.Logger;

@JSOptionClass(name = "CheckDailyScheduleOptions", description = "Checking a DailySchedule with runs in History")
public class CheckDailyScheduleOptions extends CheckDailyScheduleOptionsSuperClass {

    @SuppressWarnings("unused")//$NON-NLS-1$
    private final String conClassName = "CheckDailyScheduleOptions";  //$NON-NLS-1$
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(CheckDailyScheduleOptions.class);

    public CheckDailyScheduleOptions() {
    } // public CheckDailyScheduleOptions

    public CheckDailyScheduleOptions(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    } // public CheckDailyScheduleOptions

    public CheckDailyScheduleOptions(HashMap<String, String> JSSettings) throws Exception {
        super(JSSettings);
    } // public CheckDailyScheduleOptions (HashMap JSSettings)

    @Override
    // CheckDailyScheduleOptionsSuperClass
    public void CheckMandatory() {
        try {
            super.CheckMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    } // public void CheckMandatory ()
}
