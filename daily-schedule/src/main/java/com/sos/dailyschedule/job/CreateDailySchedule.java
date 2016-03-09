package com.sos.dailyschedule.job;

import java.io.File;
import org.apache.log4j.Logger;
import com.sos.JSHelper.Basics.IJSCommands;
import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.dailyschedule.db.Calendar2DB;

public class CreateDailySchedule extends JSJobUtilitiesClass<CreateDailyScheduleOptions> implements IJSCommands {

    private final String conClassName = "CreateDailySchedule";						//$NON-NLS-1$
    private static Logger logger = Logger.getLogger(CreateDailySchedule.class);

    public CreateDailySchedule() {
        super(new CreateDailyScheduleOptions());
    }

    @Override
    public CreateDailyScheduleOptions getOptions() {

        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::Options"; //$NON-NLS-1$

        if (objOptions == null) {
            objOptions = new CreateDailyScheduleOptions();
        }
        return objOptions;
    }

    public CreateDailySchedule Execute() throws Exception {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::Execute"; //$NON-NLS-1$

        try {
            getOptions().CheckMandatory();
            logger.debug(getOptions().dirtyString());

            Calendar2DB calendar2Db = new Calendar2DB(new File(objOptions.getconfiguration_file().Value()));
            calendar2Db.setOptions(objOptions);
            calendar2Db.store();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception(e);
        }

        finally {
        }

        return this;
    }

    public void init() {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::init"; //$NON-NLS-1$
        doInitialize();
    }

    private void doInitialize() {
    } // doInitialize

} // class CreateDailySchedule
