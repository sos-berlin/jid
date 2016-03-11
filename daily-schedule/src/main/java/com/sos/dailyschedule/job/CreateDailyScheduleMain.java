package com.sos.dailyschedule.job;

import org.apache.log4j.Logger;
import com.sos.JSHelper.Basics.JSToolBox;

public class CreateDailyScheduleMain extends JSToolBox {

    private static final Logger LOGGER = Logger.getLogger(CreateDailyScheduleMain.class);
    protected CreateDailyScheduleOptions objOptions = null;

    public final static void main(String[] pstrArgs) {

        final String conMethodName = "CreateDailyScheduleMain::Main";
        LOGGER.info("CreateDaysSchedule - Main");
        try {
            CreateDailySchedule objM = new CreateDailySchedule();
            CreateDailyScheduleOptions objO = objM.getOptions();
            objO.CommandLineArgs(pstrArgs);
            objM.Execute();
        } catch (Exception e) {
            System.err.println(conMethodName + ": " + "Error occured ..." + e.getMessage());
            LOGGER.error(e.getMessage(), e);
            int intExitCode = 99;
            LOGGER.error(String.format("JSJ-E-105: %1$s - terminated with exit-code %2$d", conMethodName, intExitCode), e);
        }
        LOGGER.info(String.format("JSJ-I-106: %1$s - ended without errors", conMethodName));
    }

}