package com.sos.dailyschedule.job;

import org.apache.log4j.Logger;
import com.sos.JSHelper.Basics.JSToolBox;

public class CreateDailyScheduleMain extends JSToolBox {


    private final static String conClassName = "CreateDaysScheduleMain"; //$NON-NLS-1$
    private static Logger logger = Logger.getLogger(CreateDailyScheduleMain.class);
    protected CreateDailyScheduleOptions objOptions = null;

    public final static void main(String[] pstrArgs) {

        final String conMethodName = conClassName + "::Main"; //$NON-NLS-1$
        logger.info("CreateDaysSchedule - Main"); //$NON-NLS-1$

        try {
            CreateDailySchedule objM = new CreateDailySchedule();
            CreateDailyScheduleOptions objO = objM.getOptions();

            objO.CommandLineArgs(pstrArgs);
            objM.Execute();
        }

        catch (Exception e) {
            System.err.println(conMethodName + ": " + "Error occured ..." + e.getMessage());
            logger.error(e.getMessage(), e);
            int intExitCode = 99;
            logger.error(String.format("JSJ-E-105: %1$s - terminated with exit-code %2$d", conMethodName, intExitCode), e);
        }

        logger.info(String.format("JSJ-I-106: %1$s - ended without errors", conMethodName));
    }

}  // class CreateDaysScheduleMain