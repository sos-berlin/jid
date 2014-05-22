package com.sos.dailyschedule.job;

import java.io.File;

import org.apache.log4j.Logger;

import sos.scheduler.job.JobSchedulerJobAdapter;
import sos.spooler.Spooler;

import com.sos.scheduler.db.SchedulerInstancesDBLayer;

/**
 * \class 		CreateDaysScheduleJSAdapterClass - JobScheduler Adapter for "Creating a DaysSchedule depending on actual Runtimes"
 *
 * \brief AdapterClass of CreateDaysSchedule for the SOSJobScheduler
 *
 * This Class CreateDaysScheduleJSAdapterClass works as an adapter-class between the SOS
 * JobScheduler and the worker-class CreateDaysSchedule.
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-2235912449518755069.html for more details.
 *
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSAdapterClass.xsl from http://www.sos-berlin.com at 20111027105329
 * \endverbatim
 */
public class CreateDailyScheduleJSAdapterClass extends JobSchedulerJobAdapter {
	private final String	conClassName	= "CreateDailyScheduleJSAdapterClass";							//$NON-NLS-1$
	private static Logger	logger			= Logger.getLogger(CreateDailyScheduleJSAdapterClass.class);

	public void init() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::init"; //$NON-NLS-1$
		logger.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName));
		doInitialize();
	}

	private void doInitialize() {
	} // doInitialize

	@Override
	public boolean spooler_init() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::spooler_init"; //$NON-NLS-1$
		logger.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName));
		return super.spooler_init();
	}

	@Override
	public boolean spooler_process() throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::spooler_process"; //$NON-NLS-1$
		logger.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName));

		try {
			super.spooler_process();
			doProcessing();
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			logger.debug("Exception:" + e.getMessage());

			return false;
		}
		finally {
		} // finally
			// return value for classic and order driven processing
			// TODO create method in base-class for this functionality
		return spooler_task.job().order_queue() != null;

	} // spooler_process

	@Override
	public void spooler_exit() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::spooler_exit"; //$NON-NLS-1$
		super.spooler_exit();
	}

	private void doProcessing() throws Exception {
		//		IJSCommands 	objJSCommands		= this;

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::doProcessing";
		logger.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName));
		CreateDailySchedule objR = new CreateDailySchedule();
		CreateDailyScheduleOptions objO = objR.Options();
		objO.setAllOptions(getSchedulerParameterAsProperties(getJobOrOrderParameters()));

		Object objSp = getSpoolerObject();
		objO.SchedulerHostName.isMandatory(true);
		objO.scheduler_port.isMandatory(true);

		Spooler objSpooler = (Spooler) objSp;
		int port = 4444;
		String host = "localhost";

		if (objO.getItem("SchedulerTcpPortNumber") != null) {
			logger.debug("port from param");
			port = objO.SchedulerTcpPortNumber.value();
		}
		else {
			logger.debug("port from scheduler");
			port = objSpooler.tcp_port();
		}

		if (objO.getItem("SchedulerHostName") != null) {
			host = objO.SchedulerHostName.Value();
		}
		else {
			host = objSpooler.hostname();
		}

		String configuration_file = "";
		if (objO.getItem("configuration_file") != null) {
			logger.debug("configuration_file from param");
			configuration_file = objO.configuration_file.Value();
		}
		else {
			logger.debug("configuration_file from scheduler");
			File f = new File(new File(objSpooler.configuration_directory()).getParent(), "hibernate.cfg.xml");
			if (!f.exists()){
	 			  f = new File(new File(objSpooler.directory()),"config/hibernate.cfg.xml");
			}
			configuration_file = f.getAbsolutePath();
		}

		objO.configuration_file.Value(configuration_file);
		objO.SchedulerHostName.Value(host);
		objO.scheduler_port.value(port);

		objO.CheckMandatory();
		objR.setJSJobUtilites(this);

		SchedulerInstancesDBLayer schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(new File(configuration_file));
		schedulerInstancesDBLayer.beginTransaction();
		schedulerInstancesDBLayer.insertScheduler(objSpooler.id(), objSpooler.hostname(), objSpooler.tcp_port());
		schedulerInstancesDBLayer.commit();

		objR.Execute();
	} // doProcessing

}
