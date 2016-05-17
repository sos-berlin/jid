package com.sos.dailyschedule.job;

import java.io.File;

import org.apache.log4j.Logger;

import sos.scheduler.job.JobSchedulerJobAdapter;
import sos.spooler.Spooler;

public class CheckDailyScheduleJSAdapterClass extends JobSchedulerJobAdapter {

    private final String conClassName = "CheckDailyScheduleJSAdapterClass";
    private static final Logger LOGGER = Logger.getLogger(CheckDailyScheduleJSAdapterClass.class);

    @Override
    public boolean spooler_process() throws Exception {
        try {
            super.spooler_process();
            doProcessing();
        } catch (Exception e) {
            return false;
        }
        return spooler_task.job().order_queue() != null;
    }

    private void doProcessing() throws Exception {
        final String conMethodName = conClassName + "::doProcessing";
        LOGGER.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName));
        CheckDailySchedule objR = new CheckDailySchedule();
        CheckDailyScheduleOptions objO = objR.getOptions();
        objO.setAllOptions(getSchedulerParameterAsProperties(getParameters()));
        Object objSp = getSpoolerObject();
        String schedulerId = "";
        String configuration_file = "";
        Spooler objSpooler = (Spooler) objSp;
        if (objO.getItem("scheduler_id") != null) {
            LOGGER.debug("scheduler_id from param");
            schedulerId = objO.scheduler_id.Value();
        } else {
            LOGGER.debug("scheduler_id from scheduler");
            schedulerId = objSpooler.id();
        }
        if (objO.configuration_file.IsEmpty() == false) {
            LOGGER.debug("configuration_file from param");
            configuration_file = objO.configuration_file.Value();
        } else {
            LOGGER.debug("configuration_file from scheduler");
            File f = new File(new File(objSpooler.configuration_directory()).getParent(), "hibernate.cfg.xml");
            if (!f.exists()) {
                f = new File(new File(objSpooler.directory()), "config/hibernate.cfg.xml");
            }
            configuration_file = f.getAbsolutePath();
        }
        if (objO.check_all_jobscheduler_instances.value()) {
            schedulerId = "";
            spooler_log.debug3("Checking all JobScheduler instance");
        }
        objO.configuration_file.Value(configuration_file);
        objO.scheduler_id.Value(schedulerId);
        objO.checkMandatory();
        objR.setJSJobUtilites(this);
        objR.Execute();
    }

}