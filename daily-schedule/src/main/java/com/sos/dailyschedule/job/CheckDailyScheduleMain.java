package com.sos.dailyschedule.job;

import com.sos.JSHelper.Basics.JSToolBox;
import org.apache.log4j.Logger;

public class CheckDailyScheduleMain extends JSToolBox {

    private static final Logger LOGGER = Logger.getLogger(CheckDailyScheduleMain.class);

    public final static void main(String[] pstrArgs) {
        final String methodName = "CheckDailyScheduleMain::Main";
        LOGGER.info("CheckDailySchedule - Main");
        try {
            CheckDailySchedule objM = new CheckDailySchedule();
            CheckDailyScheduleOptions objO = objM.Options();
            objO.CommandLineArgs(pstrArgs);
            objM.Execute();
        } catch (Exception e) {
            System.err.println(methodName + ": " + "Error occured ..." + e.getMessage());
            LOGGER.error(e.getMessage(), e);
            int intExitCode = 99;
            LOGGER.error(String.format("JSJ-E-105: %1$s - terminated with exit-code %2$d", methodName, intExitCode), e);
        }
        LOGGER.info(String.format("JSJ-I-106: %1$s - ended without errors", methodName));
    }

}