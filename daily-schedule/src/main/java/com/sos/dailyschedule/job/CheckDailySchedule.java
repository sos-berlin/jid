package com.sos.dailyschedule.job;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.dailyschedule.db.DailyScheduleAdjustment;

public class CheckDailySchedule extends JSJobUtilitiesClass<CheckDailyScheduleOptions> {

    private final String conClassName = "CheckDailySchedule";  //$NON-NLS-1$
    private static Logger logger = Logger.getLogger(CheckDailySchedule.class);

    public CheckDailySchedule() {
        super(new CheckDailyScheduleOptions());
    }

    @Override
    public CheckDailyScheduleOptions getOptions() {

        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::Options";  //$NON-NLS-1$

        if (objOptions == null) {
            objOptions = new CheckDailyScheduleOptions();
        }
        return objOptions;
    }

    public CheckDailySchedule Execute() throws Exception {
        final String conMethodName = conClassName + "::Execute";  //$NON-NLS-1$

        try {
            getOptions().CheckMandatory();
            logger.debug(getOptions().dirtyString());
            DailyScheduleAdjustment dailyScheduleAdjustment = new DailyScheduleAdjustment(new File(objOptions.configuration_file.Value()));
            dailyScheduleAdjustment.setOptions(objOptions);
            // It is not neccessary to check into the future.
            dailyScheduleAdjustment.setTo(new Date());
            dailyScheduleAdjustment.adjustWithHistory();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception(e);
        } finally {

        }

        return this;
    }

    public void init() {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::init";  //$NON-NLS-1$
        doInitialize();
    }

    private void doInitialize() {
    } // doInitialize

}  // class CheckDailySchedule