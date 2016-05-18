package com.sos.dailyschedule.job;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.IJSCommands;
import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.dailyschedule.db.Calendar2DB;

public class CreateDailySchedule extends JSJobUtilitiesClass<CreateDailyScheduleOptions> implements IJSCommands {

    private static final Logger LOGGER = Logger.getLogger(CreateDailySchedule.class);

    public CreateDailySchedule() {
        super(new CreateDailyScheduleOptions());
    }

    @Override
    public CreateDailyScheduleOptions getOptions() {
        if (objOptions == null) {
            objOptions = new CreateDailyScheduleOptions();
        }
        return objOptions;
    }

    public CreateDailySchedule Execute() throws Exception {
        try {
            getOptions().checkMandatory();
            LOGGER.debug(getOptions().dirtyString());
            Calendar2DB calendar2Db = new Calendar2DB(objOptions.getconfiguration_file().Value());
            calendar2Db.setOptions(objOptions);
            calendar2Db.store();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new Exception(e);
        }
        return this;
    }

}
