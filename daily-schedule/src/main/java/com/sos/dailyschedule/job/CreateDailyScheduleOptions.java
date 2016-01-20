package com.sos.dailyschedule.job;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;

import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import org.apache.log4j.Logger;

@JSOptionClass(name = "CreateDaysScheduleOptions", description = "Creating a DaysSchedule depending on actual Runtimes")
public class CreateDailyScheduleOptions extends CreateDailyScheduleOptionsSuperClass {

    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")//$NON-NLS-1$
    private final String conClassName = "CreateDailyScheduleOptions";  //$NON-NLS-1$
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(CreateDailyScheduleOptions.class);

    public CreateDailyScheduleOptions() {
    } // public CreateDaysScheduleOptions

    public CreateDailyScheduleOptions(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    } // public CreateDaysScheduleOptions

    public CreateDailyScheduleOptions(HashMap<String, String> JSSettings) throws Exception {
        super(JSSettings);
    } // public CreateDaysScheduleOptions (HashMap JSSettings)

    @Override
    public void CheckMandatory() {

        try {
            super.CheckMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    } // public void CheckMandatory ()
}
