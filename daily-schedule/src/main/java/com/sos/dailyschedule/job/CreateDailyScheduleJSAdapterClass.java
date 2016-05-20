package com.sos.dailyschedule.job;

import java.io.File;

import org.apache.log4j.Logger;

import sos.scheduler.job.JobSchedulerJobAdapter;
import sos.spooler.Spooler;

import com.sos.scheduler.db.SchedulerInstancesDBLayer;

public class CreateDailyScheduleJSAdapterClass extends JobSchedulerJobAdapter {

    private static final String CLASSNAME = "CreateDailyScheduleJSAdapterClass";
    private static final Logger LOGGER = Logger.getLogger(CreateDailyScheduleJSAdapterClass.class);

    @Override
    public boolean spooler_init() {
        final String conMethodName = CLASSNAME + "::spooler_init";
        LOGGER.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName));
        return super.spooler_init();
    }

    @Override
    public boolean spooler_process() throws Exception {
        final String conMethodName = CLASSNAME + "::spooler_process";
        LOGGER.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName));
        try {
            super.spooler_process();
            doProcessing();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.debug("Exception:" + e.getMessage());
            return false;
        }
        return spooler_task.job().order_queue() != null;
    }

    private void doProcessing() throws Exception {
        final String conMethodName = CLASSNAME + "::doProcessing";
        LOGGER.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName));
        CreateDailySchedule objR = new CreateDailySchedule();
        CreateDailyScheduleOptions objO = objR.getOptions();
        objO.setAllOptions(getSchedulerParameterAsProperties(getJobOrOrderParameters()));
        Object objSp = getSpoolerObject();
        objO.SchedulerHostName.isMandatory(true);
        objO.scheduler_port.isMandatory(true);
        Spooler objSpooler = (Spooler) objSp;
        int port = 4444;
        String host = "localhost";
        if (objO.getItem("SchedulerTcpPortNumber") != null) {
            LOGGER.debug("port from param");
            port = objO.SchedulerTcpPortNumber.value();
        } else {
            LOGGER.debug("port from scheduler");
            port = objSpooler.tcp_port();
        }
        if (objO.getItem("SchedulerHostName") != null) {
            host = objO.SchedulerHostName.getValue();
        } else {
            host = objSpooler.hostname();
        }
        String configuration_file = "";
        if (objO.getItem("configuration_file") != null) {
            LOGGER.debug("configuration_file from param");
            configuration_file = objO.configuration_file.getValue();
        } else {
            LOGGER.debug("configuration_file from scheduler");
            File f = new File(new File(objSpooler.configuration_directory()).getParent(), "hibernate.cfg.xml");
            if (!f.exists()) {
                f = new File(new File(objSpooler.directory()), "config/hibernate.cfg.xml");
            }
            configuration_file = f.getAbsolutePath();
        }
        objO.configuration_file.setValue(configuration_file);
        objO.SchedulerHostName.setValue(host);
        objO.scheduler_port.value(port);
        objO.checkMandatory();
        objR.setJSJobUtilites(this);
        SchedulerInstancesDBLayer schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(new File(configuration_file));
        schedulerInstancesDBLayer.beginTransaction();
        schedulerInstancesDBLayer.insertScheduler(schedulerInstancesDBLayer.setInstancesDbItemValues(host, port, objSpooler));
        schedulerInstancesDBLayer.commit();
        objR.Execute();
    }

}