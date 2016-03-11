package com.sos.dailyschedule.job;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.dailyschedule.db.DailyScheduleAdjustment;

public class CheckDailySchedule extends JSJobUtilitiesClass<CheckDailyScheduleOptions> {

    private static final Logger LOGGER = Logger.getLogger(CheckDailySchedule.class);

    public CheckDailySchedule() {
        super(new CheckDailyScheduleOptions());
    }

    @Override
    public CheckDailyScheduleOptions getOptions() {
        if (objOptions == null) {
            objOptions = new CheckDailyScheduleOptions();
        }
        return objOptions;
    }

    public CheckDailySchedule Execute() throws Exception {
        try {
            getOptions().CheckMandatory();
            LOGGER.debug(getOptions().dirtyString());
            DailyScheduleAdjustment dailyScheduleAdjustment = new DailyScheduleAdjustment(new File(objOptions.configuration_file.Value()));
            dailyScheduleAdjustment.setOptions(objOptions);
            dailyScheduleAdjustment.setTo(new Date());
            dailyScheduleAdjustment.adjustWithHistory();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new Exception(e);
        }
        return this;
    }

}